package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item

fun createLookAtItemInInventoryAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.ItemInInventory),
    ) { _, mob, context, _ ->
        val target = context[1] as Item
        Response(mob, target.description)
    }
}
