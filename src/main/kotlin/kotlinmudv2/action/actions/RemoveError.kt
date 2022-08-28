package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition

fun createRemoveErrorAction(): Action {
    return Action(
        Command.Remove,
        listOf(Syntax.Command, Syntax.FreeForm),
        alertDisposition(),
    ) { request ->
        request.respondError("you are not wearing that.")
    }
}
