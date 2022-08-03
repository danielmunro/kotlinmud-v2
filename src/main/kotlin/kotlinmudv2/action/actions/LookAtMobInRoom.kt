package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.alertDisposition

fun createLookAtMobInRoomAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.MobInRoom),
        alertDisposition(),
    ) { _, mob, context, _ ->
        val target = context[1] as Mob
        val equipped = "\n\nEquipped:\n" + target.equipped.fold("") { _, it -> it.brief + "\n" }
        Response(mob, target.description + if (target.equipped.isNotEmpty()) equipped else "")
    }
}
