package kotlinmudv2.action.actions.informational

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition
import kotlinmudv2.room.ExitStatus

fun createLookAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { request ->
        request.getRoom()?.let {
            val exits = "[Exits: ${it.exits.filter{ exit -> exit.status == null || exit.status == ExitStatus.Open }.joinToString(""){ e -> e.direction.name.substring(0, 1) }}]"
            val items = it.items.joinToString("\n") { item -> item.brief } + if (it.items.size > 0) "\n" else ""
            val mobs = request.getMobsInRoom().filter { m -> m != request.mob }.joinToString("\n") { mob -> mob.brief }
            request.respond(
                "${it.name}\n${it.description}\n$exits\n$items$mobs",
            )
        } ?: request.respondError(
            "Room was not found",
        )
    }
}
