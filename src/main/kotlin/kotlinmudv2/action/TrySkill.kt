package kotlinmudv2.action

import kotlinmudv2.mob.Mob
import kotlinmudv2.skill.Skill

fun trySkill(
    request: Request,
    skill: Skill,
    level: Int,
    specifiedTarget: Mob?,
): Response {
    if (!skill.canApplyCosts(request.mob)) {
        return request.respondError("you are too tired and cannot do that")
    }
    if (!doTargeting(request.mob, skill, specifiedTarget)) {
        if (specifiedTarget == null) {
            return request.respondError("who are you trying to target?")
        }
        return request.respondError("you are already targeting someone else!")
    }
    skill.applyCosts(request.mob)
    val target = if (specifiedTarget != null) {
        specifiedTarget
    } else if (skill.isOffensive) {
        request.mob.target
    } else {
        request.mob
    }!!
    if (!skill.rollCheck(request)) {
        if (target != request.mob) {
            return request.respondFailureWithTarget(
                skill.failureMessages[0].format(target),
                skill.failureMessages[1].format(request.mob, target),
                target,
                skill.failureMessages[2].format(request.mob),
            )
        }
        return request.respondFailure(
            skill.failureMessages[0].format(target),
            skill.failureMessages[1].format(request.mob, target),
        )
    }
    return skill.execute(request, target, level)
}
