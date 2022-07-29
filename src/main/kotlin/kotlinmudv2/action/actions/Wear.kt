package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType

fun createWearAction(): Action {
    return Action(
        Command.Wear,
        listOf(Syntax.Command, Syntax.ItemInInventory),
    ) { _, mob, context, _ ->
        val item = context[1] as Item
        if (item.itemType != ItemType.Equipment) {
            return@Action Response(
                mob,
                "that is not equipment."
            )
        }
        mob.items.remove(item)
        mob.equipped.add(item)
        Response(
            mob,
            "you wear ${item.name}."
        )
    }
}
