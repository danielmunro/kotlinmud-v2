package kotlinmudv2.skill

import kotlinmudv2.action.Request
import kotlinmudv2.action.Response
import kotlinmudv2.mob.Role

class Spell(
    name: SkillName,
    roles: List<Role>,
    level: Int,
    costs: List<Pair<Cost, Int>>,
    isOffensive: Boolean,
    rollCheck: (Request) -> Boolean,
    execute: (Request, Any, Int) -> Response,
) : Skill(
    name,
    roles,
    level,
    costs,
    listOf(
        "you lose your concentration",
        "%s loses their concentration",
        "%s loses their concentration",
    ),
    isOffensive,
    rollCheck,
    execute,
)
