package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.actions.informational.createLookAction
import kotlinmudv2.action.errorResponse
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.Direction
import kotlinmudv2.room.ExitStatus

private fun createMoveAction(command: Command, direction: Direction): Action {
    return Action(
        command,
        listOf(Syntax.Command),
        listOf(Disposition.Standing),
    ) { request ->
        if (request.mob.target != null) {
            return@Action errorResponse(
                request.mob,
                "you are fighting and can't do that!",
            )
        }
        val amount = if (request.mob.affects.find { it.type == AffectType.Hamstrung } != null)
            request.mob.calc(Attribute.Moves) / 20
        else
            1
        if (request.mob.moves < amount) {
            return@Action errorResponse(
                request.mob,
                "you are too tired to move.",
            )
        }
        val exit = request.getRoom()?.exits?.find { it.direction == direction }
        if (exit?.keyword != null && exit.status != ExitStatus.Open) {
            return@Action Response(
                request.mob,
                "the ${exit.keyword} is ${exit.status?.name?.lowercase()}",
            )
        }
        request.mob.moves -= amount
        request.moveMob(direction)?.let {
            createLookAction().execute(request)
        } ?: errorResponse(request.mob, "Alas, that direction does not exist.")
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
