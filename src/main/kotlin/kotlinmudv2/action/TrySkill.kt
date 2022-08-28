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
        return request.respondError("you are already targeting someone else!")
    }
    skill.applyCosts(request.mob)
    if (!skill.rollCheck(request)) {
        if (specifiedTarget != null) {
            request.respondFailureWithTarget(
                skill.failureMessages[0].format(specifiedTarget),
                skill.failureMessages[1].format(request.mob, specifiedTarget),
                specifiedTarget,
                skill.failureMessages[2].format(request.mob),
            )
        }
        return request.respondFailure(
            skill.failureMessages[0].format(specifiedTarget),
            skill.failureMessages[1].format(request.mob, specifiedTarget),
        )
    }
    return skill.execute(request, level)
}
