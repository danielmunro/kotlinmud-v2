package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.MaxLevel
import kotlinmudv2.mob.PlayerMob
import kotlinmudv2.mob.alertDisposition

fun createLevelAction(): Action {
    return Action(
        Command.Level,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { request ->
        if (!(request.mob as PlayerMob).debitLevel) {
            return@Action request.respondError("you have no debit levels available")
        }
        if (request.mob.level == MaxLevel) {
            return@Action request.respondError(
                "you are already the maximum level"
            )
        }
        request.mob.experience -= request.mob.experiencePerLevel
        request.mob.debitLevel = false
        request.mob.level += 1
        request.respond("you gained a level!")
    }
}
