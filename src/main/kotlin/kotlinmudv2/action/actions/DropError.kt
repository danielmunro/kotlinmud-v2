package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition

fun createDropErrorAction(): Action {
    return Action(
        Command.Drop,
        listOf(Syntax.Command, Syntax.FreeForm),
        alertDisposition(),
    ) { _, mob, _, _ ->
        Response(
            mob,
            "you don't have that",
        )
    }
}
