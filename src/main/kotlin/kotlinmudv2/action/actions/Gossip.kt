package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.anyDisposition

fun createGossipAction(): Action {
    return Action(
        Command.Gossip,
        listOf(Syntax.Command, Syntax.FreeForm),
        anyDisposition(),
    ) { actionService, mob, context, _ ->
        val gossip = context[1] as String
        actionService.getClients().filter {
            it.mob != mob
        }.forEach {
            it.write("${mob.name} gossips, \"$gossip\"")
        }
        Response(mob, "you gossip, \"$gossip\"")
    }
}
