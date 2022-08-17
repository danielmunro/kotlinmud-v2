package kotlinmudv2.skill.skills

import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createBashSkill(): Skill {
    return Skill(
        SkillName.Bash,
        listOf(Role.Warrior),
        1,
        listOf(
            Pair(Cost.Moves, 20),
            Pair(Cost.Delay, 2),
        ),
    )
}
