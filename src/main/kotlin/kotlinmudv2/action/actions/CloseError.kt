package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.errorResponse
import kotlinmudv2.mob.anyDisposition

fun createCloseErrorAction(): Action {
    return Action(
        Command.Close,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { _, mob, _, _ ->
        errorResponse(
            mob,
            "you can't close anything like that.",
        )
    }
}
