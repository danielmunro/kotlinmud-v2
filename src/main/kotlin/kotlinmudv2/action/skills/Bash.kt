package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.SkillContext
import kotlinmudv2.action.Syntax
import kotlinmudv2.dice.d4
import kotlinmudv2.game.Affect
import kotlinmudv2.mob.Disposition

fun createBashAction(): Action {
    return Action(
        Command.Bash,
        listOf(Syntax.Skill),
        listOf(Disposition.Fighting),
    ) { actionService, mob, context, _ ->
        val ctx = context[0] as SkillContext
        if (!actionService.applySkillCosts(mob, ctx)) {
            return@Action Response(
                mob,
                "you are too tired and cannot do that",
            )
        }
        if (d4() == 1) {
            return@Action Response(
                mob,
                "you fall flat on your face!",
                ActionStatus.Failure,
            )
        }
        mob.target?.also {
            it.affects[Affect.Stun] = (it.affects[Affect.Stun] ?: 0) + (ctx.level / 3).coerceAtLeast(1)
        }
        Response(
            mob,
            "you slam into ${mob.target!!.name} and send them flying!",
        )
    }
}
