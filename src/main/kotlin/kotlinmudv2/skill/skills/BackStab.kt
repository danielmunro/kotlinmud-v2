package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlinmudv2.socket.RoomMessage
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun createBackStabSkill(): Skill {
    return Skill(
        SkillName.BackStab,
        listOf(Role.Thief),
        1,
        listOf(
            Pair(Cost.Moves, 100),
            Pair(Cost.Delay, 2),
        ),
        "",
        "your backstab misses %s harmlessly",
        true,
        { _, mob ->
            val dexDiff = (mob.target?.attributes?.get(Attribute.Dex) ?: 0) - (mob.attributes[Attribute.Dex] ?: 0)
            d20() > 5 + dexDiff
        }
    ) { actionService, mob, level ->
        mob.target?.let { target ->
            val amount = Random.nextInt(level - 5, level + 5)
            runBlocking {
                actionService.doHit(
                    Hit(
                        mob,
                        target,
                        amount,
                        mob.damageType(),
                        RoomMessage(
                            mob,
                            "you stab ${target.name} in the back, making them gasp in pain!",
                            "${mob.name} stabs ${target.name} in the back, making them gasp in pain!",
                            target,
                            "${mob.name} stabs you in the back, making you gasp in pain!",
                        )
                    )
                )
            }
        }
    }
}
