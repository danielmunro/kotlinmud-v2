package kotlinmudv2.action

import kotlinmudv2.action.skills.tooTired
import kotlinmudv2.mob.Mob
import kotlinmudv2.skill.Skill

fun trySkill(
    actionService: ActionService,
    mob: Mob,
    skill: Skill,
    level: Int,
    specifiedTarget: Mob?,
): Response {
    if (!skill.canApplyCosts(mob)) {
        return tooTired(mob)
    }
    if (!doTargeting(mob, skill, specifiedTarget)) {
        return errorResponse(
            mob,
            "you are already targeting someone else!",
        )
    }
    skill.applyCosts(mob)
    if (!skill.rollCheck(actionService, mob)) {
        return failResponse(
            mob,
            skill.failure.format(mob.target!!.name),
        )
    }
    return skill.execute(actionService, mob, level)
}
