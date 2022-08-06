package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.alertDisposition

fun createListAction(): Action {
    return Action(
        Command.List,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { actionService, mob, context, input ->
//        actionService.getMobsInRoom(mob.roomId).find {
//            it.
//        }
        Response(
            mob,
            "todo implement",
        )
    }
}
