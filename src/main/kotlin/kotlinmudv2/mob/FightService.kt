package kotlinmudv2.mob

import kotlinmudv2.dice.d20
import kotlinmudv2.event.EventService
import kotlinmudv2.event.createHitEvent
import kotlinmudv2.game.Attribute
import kotlinmudv2.game.DamageType
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage
import kotlinx.coroutines.runBlocking

class FightService(
    private val mobService: MobService,
    private val clientService: ClientService,
    private val deathService: DeathService,
    private val eventService: EventService,
) {
    fun execute() {
        mobService.getFightingMobs().forEach { attacker ->
            attacker.target?.also { target ->
                if (attacker.roomId != target.roomId) {
                    attacker.target = null
                    attacker.disposition = Disposition.Standing
                    return@also
                }

                attacker.disposition = Disposition.Fighting
                target.disposition = Disposition.Fighting
                if (target.target == null) {
                    target.target = attacker
                }

                if (d20() + ((target.calc(Attribute.Dex) - attacker.calc(Attribute.Hit)) / 5) > 10) {
                    clientService.sendToRoom(
                        RoomMessage(
                            attacker,
                            "You swing at ${target.name}, missing harmlessly",
                            "${attacker.name} swings at ${target.name}, but misses",
                            target,
                            "${attacker.name} swings at you, but misses",
                        )
                    )
                    return@also
                }

                val damage = attacker.calc(Attribute.Dam)
                runBlocking {
                    eventService.publish(
                        createHitEvent(
                            Hit(
                                attacker,
                                target,
                                damage,
                                DamageType.Bash,
                            )
                        )
                    )
                }
            }
        }
        clientService.getClients().filter {
            it.mob?.target != null
        }.forEach { client ->
            client.mob?.target?.getHealthIndication()?.let { client.write("$it\n") }
        }
    }
}
