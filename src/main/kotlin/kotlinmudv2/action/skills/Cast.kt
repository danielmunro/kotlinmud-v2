package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.skill.Skill

fun createCastAction(): Action {
    return Action(
        Command.Cast,
        listOf(Syntax.Command, Syntax.Skill),
        listOf(Disposition.Fighting, Disposition.Standing),
    ) { _, mob, context, _ ->
        val skill = context[1] as Skill
        Response(
            mob,
            "you cast",
        )
    }
}
