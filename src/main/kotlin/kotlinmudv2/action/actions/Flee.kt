package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.room.Direction

fun createFleeAction(): Action {
    return Action(
        Command.Flee,
        listOf(Syntax.Command),
    ) { actionService, mob, _, _ ->
        if (mob.target == null) {
            return@Action Response(mob, "you aren't fighting anyone.", ActionStatus.Error)
        }
        val room = actionService.getRoom(mob.roomId)!!
        listOfNotNull(
            room.northId?.let { Direction.North },
            room.southId?.let { Direction.South },
            room.eastId?.let { Direction.East },
            room.westId?.let { Direction.West },
            room.upId?.let { Direction.Up },
            room.downId?.let { Direction.Down },
        ).randomOrNull()?.let {
            actionService.moveMob(mob, it)
            Response(mob, "you flee running scared!")
        } ?: Response(mob, "you have nowhere to flee!", ActionStatus.Error)
    }
}
