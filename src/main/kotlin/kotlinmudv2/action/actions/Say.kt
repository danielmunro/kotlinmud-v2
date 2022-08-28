package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.anyDisposition

fun createSayAction(): Action {
    return Action(
        Command.Say,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { request ->
        val say = request.context[1] as String
        request.getClients().filter {
            it.mob?.roomId == request.mob.roomId && it.mob != request.mob
        }.forEach {
            it.write("${request.mob} says, \"$say\"")
        }
        request.respond("you say, \"$say\"")
    }
}
