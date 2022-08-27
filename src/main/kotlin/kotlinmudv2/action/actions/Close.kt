package kotlinmudv2.action.actions

import kotlinmudv2.action.ActionService
import kotlinmudv2.action.Response
import kotlinmudv2.action.errorResponse
import kotlinmudv2.mob.Mob
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus

fun close(actionService: ActionService, exit: Exit, mob: Mob): Response {
    if (exit.status == ExitStatus.Locked) {
        return errorResponse(
            mob,
            "the ${exit.keyword} is locked"
        )
    }
    if (exit.status == ExitStatus.Closed) {
        return errorResponse(
            mob,
            "the ${exit.keyword} is already closed"
        )
    }
    exit.status = ExitStatus.Closed
    actionService.getRoom(exit.roomId)?.exits?.find { it.roomId == mob.roomId }?.status = ExitStatus.Closed
    return Response(
        mob,
        "you close the ${exit.keyword}",
        "$mob closes the ${exit.keyword}",
    )
}
