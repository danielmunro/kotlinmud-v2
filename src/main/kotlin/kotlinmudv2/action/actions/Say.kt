package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.anyDisposition

fun createSayAction(): Action {
    return Action(
        Command.Say,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { actionService, mob, context, _ ->
        val say = context[1] as String
        actionService.getClients().filter {
            it.mob?.roomId == mob.roomId && it.mob != mob
        }.forEach {
            it.write("${mob.name} says, \"$say\"")
        }
        Response(mob, "you say, \"$say\"")
    }
}
