package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item

fun createRemoveAction(): Action {
    return Action(
        Command.Remove,
        listOf(Syntax.Command, Syntax.EquippedItem)
    ) { _, mob, context, _ ->
        val item = context[1] as Item
        mob.equipped.remove(item)
        mob.items.add(item)
        Response(
            mob,
            "you remove ${item.name}."
        )
    }
}
