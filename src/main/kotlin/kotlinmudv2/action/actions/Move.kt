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
        if (mob.target != null) {
            return@Action Response(
                mob,
                "you are fighting and can't do that!",
                ActionStatus.Error,
            )
        }
        actionService.moveMob(mob, direction)?.let {
            createLookAction().execute(actionService, mob, context, "look")
        } ?: Response(mob, "Alas, that direction does not exist.", ActionStatus.Error)
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
