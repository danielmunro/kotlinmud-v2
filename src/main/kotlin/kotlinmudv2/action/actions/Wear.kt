package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.alertDisposition

fun createWearAction(): Action {
    return Action(
        Command.Wear,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { _, mob, context, _ ->
        val item = context[1] as Item
        if (item.itemType != ItemType.Equipment) {
            return@Action Response(
                mob,
                "that is not equipment."
            )
        }
        if (item.level < mob.level) {
            return@Action Response(
                mob,
                "you are not a high enough level to wear that.",
            )
        }
        var removed = ""
        mob.equipped.find {
            it.position == item.position
        }?.also {
            mob.equipped.remove(it)
            mob.items.add(it)
            removed = " remove ${it.name} and"
        }
        mob.items.remove(item)
        mob.equipped.add(item)
        Response(
            mob,
            "you$removed wear ${item.name}."
        )
    }
}
