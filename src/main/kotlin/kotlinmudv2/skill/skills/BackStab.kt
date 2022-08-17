package kotlinmudv2.skill.skills

import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createBackStabSkill(): Skill {
    return Skill(
        SkillName.BackStab,
        listOf(Role.Thief),
        1,
        listOf(
            Pair(Cost.Moves, 100),
            Pair(Cost.Delay, 2),
        ),
    )
}
