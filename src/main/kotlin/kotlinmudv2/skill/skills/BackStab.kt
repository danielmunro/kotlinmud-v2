package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
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
        "you stab %s in the back, making them gasp in pain!",
        "your backstab misses %s harmlessly",
        { _, mob ->
            val dexDiff = (mob.target?.attributes?.get(Attribute.Dex) ?: 0) - (mob.attributes[Attribute.Dex] ?: 0)
            d20() > 5 + dexDiff
        }
    ) { _, mob, level ->
        mob.target?.let { target ->
            target.hp -= Random.nextInt(level - 5, level + 5)
        }
    }
}
