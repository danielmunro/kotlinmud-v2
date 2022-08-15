package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.anyDisposition

fun createOpenErrorAction(): Action {
    return Action(
        Command.Open,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { _, mob, _, _ ->
        Response(
            mob,
            "you can't open anything like that.",
        )
    }
}
