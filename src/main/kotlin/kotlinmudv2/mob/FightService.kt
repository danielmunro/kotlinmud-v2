package kotlinmudv2.mob

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage

class FightService(
    private val mobService: MobService,
    private val clientService: ClientService,
    private val deathService: DeathService,
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
                target.hp -= damage

                clientService.sendToRoom(
                    RoomMessage(
                        attacker,
                        "You hit ${target.name}, causing scratches",
                        "${attacker.name} hit ${target.name}, causing scratches",
                        target,
                        "${attacker.name} hit you, causing scratches",
                    )
                )

                deathService.damageReceived(attacker, target)
            }
        }
        clientService.getClients().filter {
            it.mob?.target != null
        }.forEach { client ->
            client.mob?.target?.getHealthIndication()?.let { client.write("$it\n") }
        }
    }
}
