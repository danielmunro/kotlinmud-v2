package kotlinmudv2.action.actions.informational

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item
import kotlinmudv2.mob.alertDisposition

fun createLookAtItemInRoomAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.ItemInRoom),
        alertDisposition(),
    ) { request ->
        val target = request.context[1] as Item
        request.respond(target.description)
    }
}
