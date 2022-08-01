package kotlinmudv2.fight

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.PlayerMob
import kotlinmudv2.room.startRoomId
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage

class FightService(
    private val mobService: MobService,
    private val clientService: ClientService,
) {
    fun execute() {
        mobService.getFightingMobs().forEach { attacker ->
            attacker.target?.also { target ->
                if (attacker.roomId != target.roomId) {
                    attacker.target = null
                    return@also
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

                if (target.hp < 0) {
                    attacker.target = null
                    handleDeath(target)
                }
            }
        }
        clientService.getClients().filter {
            it.mob?.target != null
        }.forEach { client ->
            client.mob?.target?.getHealthIndication()?.let { client.write(it) }
        }
    }

    private fun handleDeath(mob: Mob) {
        if (mob is PlayerMob) {
            clientService.sendToRoom(
                RoomMessage(
                    mob,
                    "You are DEAD!",
                    "${mob.name} has died!",
                )
            )
            mob.hp = 0
            mob.roomId = startRoomId
            mob.target = null
            return
        }
        mobService.removeMob(mob)
    }
}
