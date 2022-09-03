package kotlinmudv2.action.actions.manipulate

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition

fun createDropErrorAction(): Action {
    return Action(
        Command.Drop,
        listOf(Syntax.Command, Syntax.FreeForm),
        alertDisposition(),
    ) { request ->
        request.respondError("you don't have that")
    }
}
