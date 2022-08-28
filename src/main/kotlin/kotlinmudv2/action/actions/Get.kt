package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemFlag
import kotlinmudv2.mob.alertDisposition

fun createGetAction(): Action {
    return Action(
        Command.Get,
        listOf(Syntax.Command, Syntax.ItemInRoom),
        alertDisposition(),
    ) { request ->
        val item = request.context[1] as Item
        if (item.flags.find { it == ItemFlag.NoGet } != null) {
            return@Action request.respondError("you cannot pick that up.")
        }
        request.getRoom()!!.also {
            it.items.remove(item)
            request.mob.items.add(item)
        }
        request.respondToRoom(
            "you pick up $item and put it in your inventory.",
            "${request.mob} picks up $item",
        )
    }
}
