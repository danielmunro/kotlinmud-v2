package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.anyDisposition

fun createGossipAction(): Action {
    return Action(
        Command.Gossip,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { request ->
        val gossip = request.context[1] as String
        request.getClients().filter {
            it.mob != request.mob
        }.forEach {
            it.write("${request.mob} gossips, \"$gossip\"")
        }
        request.respond("you gossip, \"$gossip\"")
    }
}
