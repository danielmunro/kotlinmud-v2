package kotlinmudv2.fight

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.PlayerMob
import kotlinmudv2.room.startRoomId
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage
import kotlin.random.Random

class FightService(
    private val mobService: MobService,
    private val clientService: ClientService,
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

                if (target.hp < 0) {
                    clientService.sendToRoom(
                        RoomMessage(
                            target,
                            "You are DEAD!",
                            "${target.name} has died!",
                        )
                    )
                    attacker.target = null
                    attacker.disposition = Disposition.Standing
                    handleDeath(target)
                    if (attacker is PlayerMob) {
                        handleExperienceGain(attacker, target)
                    }
                }
            }
        }
        clientService.getClients().filter {
            it.mob?.target != null
        }.forEach { client ->
            client.mob?.target?.getHealthIndication()?.let { client.write("$it\n") }
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
