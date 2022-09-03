package kotlinmudv2.action.actions.manipulate

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.anyDisposition

fun createOpenErrorAction(): Action {
    return Action(
        Command.Open,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { request ->
        request.respondError("you can't open anything like that.")
    }
}
