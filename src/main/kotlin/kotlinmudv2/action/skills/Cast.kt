package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.SkillContext
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.failResponse
import kotlinmudv2.mob.Disposition
import kotlinmudv2.skill.Skill

fun createCastAction(): Action {
    return Action(
        Command.Cast,
        listOf(Syntax.Command, Syntax.Skill),
        listOf(Disposition.Fighting, Disposition.Standing),
    ) { actionService, mob, context, _ ->
        val ctx = context[1] as SkillContext
        if (!ctx.skill.canApplyCosts(mob)) {
            return@Action tooTired(mob)
        }
        ctx.skill.applyCosts(mob)
        if (!ctx.skill.rollCheck(actionService, mob)) {
            return@Action failResponse(
                mob,
                ctx.skill.failure.format(mob.target!!.name),
            )
        }
        ctx.skill.execute(actionService, mob, ctx.level)
        Response(
            mob,
            ctx.skill.success.format(mob.target!!.name),
        )
    }
}
