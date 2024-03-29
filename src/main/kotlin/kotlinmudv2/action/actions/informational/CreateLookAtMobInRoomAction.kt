package kotlinmudv2.action.actions.informational

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.game.AffectType
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.alertDisposition

fun createLookAtMobInRoomAction(): Action {
    return Action(
        Command.Look,
        listOf(Syntax.Command, Syntax.MobInRoom),
        alertDisposition(),
    ) { request ->
        val target = request.context[1] as Mob
        if (target.affectedBy(AffectType.Sneak)) {
            return@Action request.respond("you don't see them anywhere.")
        }
        val equipped = "\n\nEquipped:\n" + target.equipped.fold("") { _, it -> it.brief + "\n" }
        request.respond(target.description + if (target.equipped.isNotEmpty()) equipped else "")
    }
}
