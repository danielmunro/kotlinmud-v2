package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.SkillContext
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition

fun createSkillAction(command: Command): Action {
    return Action(
        command,
        listOf(Syntax.Skill),
        listOf(Disposition.Fighting),
    ) { actionService, mob, context, _ ->
        val ctx = context[0] as SkillContext
        if (!ctx.skill.canApplyCosts(mob)) {
            return@Action tooTired(mob)
        }
        ctx.skill.applyCosts(mob)
        if (!ctx.skill.rollCheck(actionService, mob)) {
            return@Action Response(
                mob,
                ctx.skill.failure.format(mob.target!!.name),
                null,
                null,
                ActionStatus.Failure,
            )
        }
        ctx.skill.execute(actionService, mob, ctx.level)
        Response(
            mob,
            ctx.skill.success.format(mob.target!!.name),
        )
    }
}
