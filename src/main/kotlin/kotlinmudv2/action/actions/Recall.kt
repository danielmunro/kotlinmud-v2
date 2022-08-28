package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.startRoomId

fun createRecallAction(): Action {
    return Action(
        Command.Recall,
        listOf(Syntax.Command),
        listOf(Disposition.Standing),
    ) { request ->
        if (request.mob.moves < 20) {
            return@Action request.respondError(
                "you are too tired.",
            )
        }
        request.mob.roomId = startRoomId
        request.mob.moves = request.mob.moves / 2
        request.respond(
            "you pray for recall.\n" +
                createLookAction().execute(request).toActionCreator,
        )
    }
}
