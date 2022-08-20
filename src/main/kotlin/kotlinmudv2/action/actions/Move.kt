package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.errorResponse
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.Direction
import kotlinmudv2.room.ExitStatus

private fun createMoveAction(command: Command, direction: Direction): Action {
    return Action(
        command,
        listOf(Syntax.Command),
        listOf(Disposition.Standing),
    ) { actionService, mob, context, _ ->
        if (mob.target != null) {
            return@Action errorResponse(
                mob,
                "you are fighting and can't do that!",
            )
        }
        if (mob.moves < 1) {
            return@Action errorResponse(
                mob,
                "you are too tired to move.",
            )
        }
        val exit = actionService.getRoom(mob.roomId)?.exits?.find { it.direction == direction }
        if (exit?.keyword != null && exit.status != ExitStatus.Open) {
            return@Action Response(
                mob,
                "the ${exit.keyword} is ${exit.status?.name?.lowercase()}",
            )
        }
        mob.moves -= 1
        actionService.moveMob(mob, direction)?.let {
            createLookAction().execute(actionService, mob, context, "look")
        } ?: errorResponse(mob, "Alas, that direction does not exist.")
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
