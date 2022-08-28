package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.mob.alertDisposition

fun createLookAtItemInInventoryAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { request ->
        val target = request.context[1] as Item
        request.respond(target.description)
    }
}
