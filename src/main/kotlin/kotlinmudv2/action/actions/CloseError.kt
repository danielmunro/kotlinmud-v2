package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.anyDisposition

fun createCloseErrorAction(): Action {
    return Action(
        Command.Close,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { request ->
        request.respondError("you can't close anything like that.")
    }
}
