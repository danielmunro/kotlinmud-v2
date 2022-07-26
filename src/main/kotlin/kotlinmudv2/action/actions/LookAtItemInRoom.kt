package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.item.Item

fun createLookAtItemInRoomAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.ItemInRoom),
    ) { _, mob, context, _ ->
        val target = context[1] as Item
        Response(mob, target.description)
    }
}
