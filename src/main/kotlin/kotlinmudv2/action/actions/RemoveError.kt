package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax

fun createRemoveErrorAction(): Action {
    return Action(
        Command.Remove,
        listOf(Syntax.Command, Syntax.FreeForm)
    ) { _, mob, _, _ ->
        Response(
            mob,
            "you are not wearing that."
        )
    }
}
