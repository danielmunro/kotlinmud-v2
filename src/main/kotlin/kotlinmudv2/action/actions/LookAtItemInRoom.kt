package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.mob.alertDisposition

fun createLookAtItemInRoomAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.ItemInRoom),
        alertDisposition(),
    ) { _, mob, context, _ ->
        val target = context[1] as Item
        Response(mob, target.description)
    }
}
