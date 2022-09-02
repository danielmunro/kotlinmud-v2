package kotlinmudv2.action.actions.commerce

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition

fun createSellErrorAction(): Action {
    return Action(
        Command.Buy,
        listOf(Syntax.Command, Syntax.FreeForm),
        alertDisposition(),
    ) { request ->
        request.respondError("what do you want to sell?")
    }
}
