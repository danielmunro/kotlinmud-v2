package kotlinmudv2.action.actions.informational

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition
import kotlinmudv2.room.ExitStatus

fun createExitsAction(): Action {
    return Action(
        Command.Exits,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { request ->
        request.respond(
            request.getRoom()!!.exits
                .filter { it.status == null || it.status == ExitStatus.Open }
                .joinToString("\n") { exit ->
                    request.getRoom(exit.roomId)!!.let {
                        "${exit.direction.name} - ${it.name}"
                    }
                }
        )
    }
}
