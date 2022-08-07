package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.MobFlag
import kotlinmudv2.mob.alertDisposition

fun createListAction(): Action {
    return Action(
        Command.List,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { actionService, mob, _, _ ->
        actionService.getMobsInRoom(mob.roomId).find {
            it.flags.contains(MobFlag.Shopkeeper)
        }?.let {
            Response(
                mob,
                "[Lv Price Qty] Item\n" + it.items.sortedBy { item -> item.level }.joinToString("\n") { item ->
                    "[${String.format("%1$2s", item.level)} ${String.format("%1$5s", item.value)}   -] ${item.name}"
                },
            )
        } ?: Response(
            mob,
            "you don't see any shopkeeper here"
        )
    }
}
