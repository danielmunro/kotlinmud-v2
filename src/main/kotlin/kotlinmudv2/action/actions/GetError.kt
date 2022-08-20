package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.errorResponse
import kotlinmudv2.mob.alertDisposition

fun createGetErrorAction(): Action {
    return Action(
        Command.Get,
        listOf(Syntax.Command, Syntax.FreeForm),
        alertDisposition(),
    ) { _, mob, _, _ ->
        errorResponse(
            mob,
            "you don't see that anywhere.",
        )
    }
}
