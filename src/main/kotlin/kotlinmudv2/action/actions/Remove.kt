package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.mob.alertDisposition

fun createRemoveAction(): Action {
    return Action(
        Command.Remove,
        listOf(Syntax.Command, Syntax.EquippedItem),
        alertDisposition(),
    ) { request ->
        val item = request.context[1] as Item
        request.mob.equipped.remove(item)
        request.mob.items.add(item)
        request.respond("you remove $item.")
    }
}
