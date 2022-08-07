package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.MobFlag
import kotlinmudv2.mob.alertDisposition

fun createListAction(): Action {
    return Action(
        Command.List,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { actionService, mob, _, _ ->
        println(mob.roomId)
        actionService.getMobsInRoom(mob.roomId).find {
            println("yo - ${it.flags.size}")
            it.flags.contains(MobFlag.Shopkeeper)
        }?.let {
            Response(
                mob,
                mob.items.joinToString("\n") { "${it.brief} - ${it.value}" },
            )
        } ?: Response (
            mob,
            "you don't see any shopkeeper here"
        )
    }
}
