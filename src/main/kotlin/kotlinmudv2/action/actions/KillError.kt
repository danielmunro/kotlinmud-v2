package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax

fun createKillErrorAction(): Action {
    return Action(
        Command.Kill,
        listOf(Syntax.Command, Syntax.FreeForm),
    ) { _, mob, _, _ ->
        Response(
            mob,
            "you don't see them anywhere.",
        )
    }
}
