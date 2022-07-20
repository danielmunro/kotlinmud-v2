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
            Response(
                mob,
                ActionStatus.Success,
                "${it.name}\n${it.description}\n"
            )
        } ?: Response(
            mob,
            ActionStatus.Error,
            "Room was not found",
        )
    }
}
