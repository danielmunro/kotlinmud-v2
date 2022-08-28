package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemFlag
import kotlinmudv2.mob.alertDisposition

fun createDropAction(): Action {
    return Action(
        Command.Drop,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { request ->
        val item = request.context[1] as Item
        if (item.flags.contains(ItemFlag.NoDrop)) {
            return@Action request.respondError("$item is bound to you and you cannot drop it")
        }
        request.mob.items.remove(item)
        request.getRoom()?.items?.add(item)
        request.respondToRoom(
            "you drop $item",
            "${request.mob} drops $item",
        )
    }
}
