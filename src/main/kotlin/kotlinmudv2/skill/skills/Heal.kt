package kotlinmudv2.skill.skills

import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createHealSkill(): Skill {
    return Skill(
        SkillName.Heal,
        listOf(Role.Cleric),
        1,
        listOf(
            Pair(Cost.Mana, 100),
            Pair(Cost.Delay, 1),
        ),
        { actionService, mob -> true },
        { actionService, mob, i -> },
    )
}
