package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemFlag
import kotlinmudv2.mob.alertDisposition

fun createDropAction(): Action {
    return Action(
        Command.Drop,
        listOf(Syntax.Command, Syntax.ItemInInventory),
        alertDisposition(),
    ) { actionService, mob, context, _ ->
        val item = context[1] as Item
        if (item.flags.contains(ItemFlag.NoDrop)) {
            return@Action Response(
                mob,
                "${item.name} is bound to you and you cannot drop it"
            )
        }
        mob.items.remove(item)
        actionService.getRoom(mob.roomId)?.items?.add(item)
        Response(
            mob,
            "you drop ${item.name}",
        )
    }
}
