package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.errorResponse
import kotlinmudv2.mob.Disposition

fun createFleeAction(): Action {
    return Action(
        Command.Flee,
        listOf(Syntax.Command),
        listOf(Disposition.Fighting),
    ) { actionService, mob, _, _ ->
        actionService.getRoom(mob.roomId)?.exits?.randomOrNull()?.let {
            actionService.moveMob(mob, it.direction)
            Response(mob, "you flee running scared!")
        } ?: errorResponse(mob, "you have nowhere to flee!")
    }
}
