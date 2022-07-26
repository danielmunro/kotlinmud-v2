package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax

fun createLookAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command)
    ) { actionService, mob, _, _ ->
        actionService.getRoom(mob.roomId)?.let {
            val exits = "[Exits: ${it.northId?.let{"N"} ?: ""}${it.southId?.let{"S"} ?: ""}${it.eastId?.let{"E"} ?: ""}${it.westId?.let{"W"} ?: ""}${it.upId?.let{"U"} ?: ""}${it.downId?.let{"D"} ?: ""}]"
            val items = it.items.joinToString("\n") { item -> item.brief }
            val mobs = actionService.getMobsInRoom(it.id).filter { m -> m != mob }.joinToString("\n") { mob -> mob.brief }
            Response(
                mob,
                "${it.name}\n${it.description}\n$exits\n$items$mobs",
            )
        } ?: Response(
            mob,
            "Room was not found",
            ActionStatus.Error,
        )
    }
}
