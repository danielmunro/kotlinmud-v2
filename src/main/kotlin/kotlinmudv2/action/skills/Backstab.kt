package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.SkillContext
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.skill.Skill

fun createBackstabAction(): Action {
    return Action(
        Command.Backstab,
        listOf(Syntax.Skill),
        listOf(Disposition.Fighting),
    ) { actionService, mob, context, _ ->
        val ctx = context[0] as SkillContext
        if (!ctx.skill.canApplyCosts(mob)) {
            return@Action tooTired(mob)
        }
        if (!ctx.skill.rollCheck(actionService, mob)) {
            return@Action Response(
                mob,
                "your backstab misses ${mob.target!!.name} harmlessly"
            )
        }
        ctx.skill.execute(actionService, mob, ctx.level)
        Response(
            mob,
            "you backstab ${mob.target!!.name} making them gasp in pain",
        )
    }
}
