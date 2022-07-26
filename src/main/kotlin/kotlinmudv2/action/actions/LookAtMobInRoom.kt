package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Mob

fun createLookAtMobInRoomAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.MobInRoom)
    ) { _, mob, context, _ ->
        val target = context[1] as Mob
        Response(mob, target.description)
    }
}
