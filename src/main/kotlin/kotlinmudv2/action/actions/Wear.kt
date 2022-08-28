package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.alertDisposition

fun createWearAction(): Action {
    return Action(
        Command.Wear,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { request ->
        val item = request.context[1] as Item
        if (item.itemType != ItemType.Equipment) {
            return@Action request.respondError("that is not equipment.")
        }
        if (item.level > request.mob.level) {
            return@Action request.respondError("you are not a high enough level to wear that.")
        }
        var removed = ""
        request.mob.equipped.find {
            it.position == item.position
        }?.also {
            request.mob.equipped.remove(it)
            request.mob.items.add(it)
            removed = " remove ${it.name} and"
        }
        request.mob.items.remove(item)
        request.mob.equipped.add(item)
        request.respondToRoom(
            "you$removed wear $item",
            "${request.mob}$removed wear $item",
        )
    }
}
