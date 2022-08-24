package kotlinmudv2.mob

import kotlinmudv2.room.startRoomId
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage
import kotlin.random.Random

class DeathService(
    private val clientService: ClientService,
    private val mobService: MobService,
) {
    fun damageReceived(attacker: Mob, defender: Mob) {
        if (defender.hp < 0) {
            clientService.sendToRoom(
                RoomMessage(
                    defender,
                    "You are DEAD!",
                    "${defender.name} has died!",
                )
            )
            attacker.target = null
            attacker.disposition = Disposition.Standing
            handleDeath(defender)
            if (attacker is PlayerMob) {
                handleExperienceGain(attacker, defender)
            }
        }
    }

    private fun handleDeath(mob: Mob) {
        if (mob is PlayerMob) {
            mob.hp = 0
            mob.roomId = startRoomId
            mob.target = null
            mob.disposition = Disposition.Standing
            return
        }
        mobService.removeMob(mob)
    }

    private fun handleExperienceGain(mob1: PlayerMob, mob2: Mob) {
        if (mob1.debitLevel) {
            clientService.sendToClientByMob(mob1, "you already qualify for a level.")
            return
        }
        val base = when (val difference = mob1.level - mob2.level) {
            -8 -> 2
            -7 -> 7
            -6 -> 13
            -5 -> 20
            -4 -> 26
            -3 -> 40
            -2 -> 60
            -1 -> 80
            0 -> 100
            1 -> 140
            2 -> 180
            3 -> 220
            4 -> 280
            5 -> 320
            else -> {
                if (difference < -8) {
                    0
                } else {
                    320 + (30 * difference - 5)
                }
            }
        }
        val randomized = Random.nextDouble((base * 4 / 5).toDouble(), (base * 6 / 5).toDouble())
        val amount = randomized.toInt()
        mob1.experience += amount
        clientService.sendToClientByMob(mob1, "you gain $amount experience.")
        if (mob1.experience > mob1.experiencePerLevel) {
            mob1.debitLevel = true
            clientService.sendToClientByMob(mob1, "you gained a level!!!")
        }
    }
}
