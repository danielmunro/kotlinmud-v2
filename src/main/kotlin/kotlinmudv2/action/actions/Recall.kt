package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.startRoomId

fun createRecallAction(): Action {
    return Action(
        Command.Recall,
        listOf(Syntax.Command),
        listOf(Disposition.Standing),
    ) { _, mob, _, _ ->
        if (mob.moves < 20) {
            return@Action Response(
                mob,
                "you are too tired.",
            )
        }
        mob.roomId = startRoomId
        mob.moves = mob.moves / 2
        Response(
            mob,
            "you pray for recall.",
        )
    }
}
