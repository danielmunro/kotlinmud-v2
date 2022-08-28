package kotlinmudv2.action.actions

import kotlinmudv2.action.Request
import kotlinmudv2.action.Response
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus

fun open(request: Request, exit: Exit): Response {
    if (exit.status == ExitStatus.Locked) {
        return request.respondError("the ${exit.keyword} is locked")
    }
    if (exit.status == ExitStatus.Open) {
        return request.respondError("the ${exit.keyword} is already open")
    }
    exit.status = ExitStatus.Open
    request.getRoom()?.exits?.find { it.roomId == request.mob.roomId }?.status = ExitStatus.Open
    return request.respond("you open the ${exit.keyword}")
}
