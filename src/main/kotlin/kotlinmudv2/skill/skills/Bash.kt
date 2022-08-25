package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlinmudv2.socket.RoomMessage
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun createBashSkill(): Skill {
    return Skill(
        SkillName.Bash,
        listOf(Role.Warrior),
        1,
        listOf(
            Pair(Cost.Moves, 20),
            Pair(Cost.Delay, 2),
        ),
        "you slam into %s and send them flying!",
        "you fall flat on your face!",
        true,
        { _, mob ->
            val sizeDiff = (mob.target?.race?.size?.value ?: 0) - mob.race.size.value
            d20() <= 5 + sizeDiff
        },
    ) { actionService, mob, level ->
        mob.target?.also { target ->
            val initial = (level / 3).coerceAtLeast(1)
            val affect = target.affects.find { it.type == AffectType.Stun }
            val impact = (level / 5).coerceAtLeast(1)
            if (affect == null) {
                target.affects.add(
                    Affect(
                        AffectType.Stun,
                        initial,
                        mutableMapOf(
                            Pair(Attribute.Int, -impact),
                        ),
                    )
                )
            } else {
                affect.timeout = (affect.timeout + initial).coerceAtMost(4)
            }
            val amount = Random.nextInt(initial - 1, initial + 1).coerceAtLeast(1)
            runBlocking {
                actionService.doHit(
                    Hit(
                        mob,
                        target,
                        amount,
                        mob.damageType(),
                        RoomMessage(
                            mob,
                            "you slam into ${target.name} and send them flying!",
                            "${mob.name} slams into ${target.name} and sends them flying!",
                            target,
                            "${mob.name} slams into you and sends you flying!"
                        )
                    )
                )
            }
        }
    }
}
