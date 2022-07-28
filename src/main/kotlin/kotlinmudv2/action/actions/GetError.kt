package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax

fun createGetErrorAction(): Action {
    return Action(
        Command.Get,
        listOf(Syntax.Command, Syntax.FreeForm),
    ) { _, mob, _, _ ->
        Response(
            mob,
            "you don't see that anywhere.",
            ActionStatus.Error,
        )
    }
}
