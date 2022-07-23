package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.room.Direction

private fun createMoveAction(command: Command, direction: Direction): Action {
    return Action(
        command,
        listOf(Syntax.Command)
    ) { actionService, mob, context, _ ->
        actionService.moveMob(mob, direction)?.let {
            createLookAction().execute(actionService, mob, context, "look")
        } ?: Response(mob, ActionStatus.Error, "Alas, that direction does not exist.")
    }
}

fun createNorthAction(): Action {
    return createMoveAction(Command.North, Direction.North)
}

fun createSouthAction(): Action {
    return createMoveAction(Command.South, Direction.South)
}

fun createEastAction(): Action {
    return createMoveAction(Command.East, Direction.East)
}

fun createWestAction(): Action {
    return createMoveAction(Command.West, Direction.West)
}

fun createUpAction(): Action {
    return createMoveAction(Command.Up, Direction.Up)
}

fun createDownAction(): Action {
    return createMoveAction(Command.Down, Direction.Down)
}
