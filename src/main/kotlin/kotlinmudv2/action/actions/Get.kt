package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.alertDisposition

fun createGetAction(): Action {
    return Action(
        Command.Get,
        listOf(Syntax.Command, Syntax.ItemInRoom),
        alertDisposition(),
    ) { actionService, mob, context, _ ->
        val item = context[1] as Item
        if (item.itemType == ItemType.Furniture) {
            return@Action Response(
                mob,
                "you cannot pick that up.",
                ActionStatus.Error,
            )
        }
        actionService.getRoom(mob.roomId)!!.also {
            it.items.remove(item)
            mob.items.add(item)
        }
        Response(
            mob,
            "you pick up ${item.name} and put it in your inventory."
        )
    }
}
