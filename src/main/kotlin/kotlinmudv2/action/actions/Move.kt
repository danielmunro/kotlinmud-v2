package kotlinmudv2.action.actions

import kotlinmudv2.action.*
import kotlinmudv2.room.Direction

fun createNorthAction(): Action {
    return Action(
        Command.North,
        listOf(Syntax.Command)
    ) { actionService, mob, _ ->
        actionService.moveMob(mob, Direction.North)?.let {
            createLookAction().execute(actionService, mob, "look")
        } ?: Response(mob, ActionStatus.Error, "Alas, that direction does not exist.")
    }
}
