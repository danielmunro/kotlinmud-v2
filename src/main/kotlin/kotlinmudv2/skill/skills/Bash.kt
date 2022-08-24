package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
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
            val amount = (level / 3).coerceAtLeast(1)
            val affect = target.affects.find { it.type == AffectType.Stun }
            val impact = (level / 5).coerceAtLeast(1)
            if (affect == null) {
                target.affects.add(
                    Affect(
                        AffectType.Stun,
                        amount,
                        mutableMapOf(
                            Pair(Attribute.Int, -impact),
                        ),
                    )
                )
            } else {
                affect.timeout = (affect.timeout + amount).coerceAtMost(4)
            }
            target.hp -= Random.nextInt(amount - 1, amount + 1).coerceAtLeast(1)
            actionService.damageReceived(mob, target)
        }
    }
}
