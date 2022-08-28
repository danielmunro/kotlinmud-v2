package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition

fun createSacrificeErrorAction(): Action {
    return Action(
        Command.Sacrifice,
        listOf(Syntax.Command, Syntax.FreeForm),
        listOf(Disposition.Standing),
    ) { request ->
        request.respondError("you don't see that anywhere.")
    }
}
