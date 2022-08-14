package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition

fun createLookAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { actionService, mob, _, _ ->
        actionService.getRoom(mob.roomId)?.let {
            val exits = "[Exits: ${it.exits.joinToString{ e -> e.direction.name.substring(0, 1) }}]"
            val items = it.items.joinToString("\n") { item -> item.brief } + if (it.items.size > 0) "\n" else ""
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
