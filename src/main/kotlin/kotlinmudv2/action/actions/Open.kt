package kotlinmudv2.action.actions

import kotlinmudv2.action.ActionService
import kotlinmudv2.action.Response
import kotlinmudv2.mob.Mob
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus

fun open(actionService: ActionService, exit: Exit, mob: Mob): Response {
    if (exit.status == ExitStatus.Locked) {
        return Response(
            mob,
            "the ${exit.keyword} is locked"
        )
    }
    if (exit.status == ExitStatus.Open) {
        return Response(
            mob,
            "the ${exit.keyword} is already open"
        )
    }
    exit.status = ExitStatus.Open
    actionService.getRoom(exit.roomId)?.exits?.find { it.roomId == mob.roomId }?.status = ExitStatus.Open
    return Response(
        mob,
        "you open the ${exit.keyword}",
    )
}
