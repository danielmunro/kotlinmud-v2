package kotlinmudv2.action.actions

import kotlinmudv2.action.Request
import kotlinmudv2.action.Response
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus

fun close(request: Request, exit: Exit): Response {
    if (exit.status == ExitStatus.Locked) {
        return request.respondError("the ${exit.keyword} is locked")
    }
    if (exit.status == ExitStatus.Closed) {
        return request.respondError("the ${exit.keyword} is already closed")
    }
    exit.status = ExitStatus.Closed
    request.getRoom(exit.roomId)?.exits?.find { it.roomId == request.mob.roomId }?.status = ExitStatus.Closed
    return request.respondToRoom(
        "you close the ${exit.keyword}",
        "${request.mob} closes the ${exit.keyword}",
    )
}
