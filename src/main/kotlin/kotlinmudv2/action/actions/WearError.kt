package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax

fun createWearErrorAction(): Action {
    return Action(
        Command.Wear,
        listOf(Syntax.Command, Syntax.FreeForm)
    ) { _, mob, _, _ ->
        Response(
            mob,
            "you don't have anything like that to wear."
        )
    }
}
