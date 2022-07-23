package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.Response
import kotlinmudv2.action.ActionStatus

fun createLookAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command)
    ) { actionService, mob, _ ->
        actionService.getRoom(mob.roomId)?.let {
            val exits = "[Exits: ${it.northId?.let{"N"} ?: ""}${it.southId?.let{"S"} ?: ""}${it.eastId?.let{"E"} ?: ""}${it.westId?.let{"W"} ?: ""}${it.upId?.let{"U"} ?: ""}${it.downId?.let{"D"} ?: ""}]"
            val items = it.items.joinToString("\n") { item -> item.brief }
            val mobs = actionService.getMobsInRoom(it.id).joinToString("\n") { mob -> mob.brief }
            Response(
                mob,
                ActionStatus.Success,
                "${it.name}\n${it.description}\n$exits\n$items\n$mobs"
            )
        } ?: Response(
            mob,
            ActionStatus.Error,
            "Room was not found",
        )
    }
}
