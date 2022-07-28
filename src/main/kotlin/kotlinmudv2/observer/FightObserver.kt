package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.PlayerMob
import kotlinmudv2.room.startRoomId
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage

class FightObserver(
    private val mobService: MobService,
    private val clientService: ClientService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.getFightingMobs().forEach { attacker ->
            attacker.target?.also { target ->
                if (attacker.roomId != target.roomId) {
                    attacker.target = null
                    return@also
                }

                target.hp -= attacker.calc(Attribute.Dam)

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
            return
        }
        mobService.removeMob(mob)
    }
}
