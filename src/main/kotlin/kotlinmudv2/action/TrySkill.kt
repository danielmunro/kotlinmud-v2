package kotlinmudv2.action

import kotlinmudv2.action.skills.tooTired
import kotlinmudv2.mob.Mob
import kotlinmudv2.skill.Skill

fun trySkill(
    request: Request,
    skill: Skill,
    level: Int,
    specifiedTarget: Mob?,
): Response {
    if (!skill.canApplyCosts(request.mob)) {
        return tooTired(request.mob)
    }
    if (!doTargeting(request.mob, skill, specifiedTarget)) {
        return request.respondError("you are already targeting someone else!")
    }
    skill.applyCosts(request.mob)
    if (!skill.rollCheck(request)) {
        return request.respondFailure(skill.failure.format(request.mob.target!!.name))
    }
    return skill.execute(request, level)
}
