package kotlinmudv2.skill

import kotlinmudv2.mob.Role

class Skill (
    val name: SkillName,
    val roles: List<Role>,
    val level: Int,
    val costs: List<Pair<Cost, Int>>,
)
