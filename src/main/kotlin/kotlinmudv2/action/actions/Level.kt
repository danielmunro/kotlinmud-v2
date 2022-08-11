package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.MaxLevel
import kotlinmudv2.mob.PlayerMob
import kotlinmudv2.mob.alertDisposition

fun createLevelAction(): Action {
    return Action(
        Command.Level,
        listOf(Syntax.Command),
        alertDisposition(),
    ) { _, mob, _, _ ->
        if (!(mob as PlayerMob).debitLevel) {
            return@Action Response(
                mob,
                "you have no debit levels available",
            )
        }
        if (mob.level == MaxLevel) {
            return@Action Response(
                mob,
                "you are already the maximum level"
            )
        }
        mob.experience -= mob.experiencePerLevel
        mob.debitLevel = false
        mob.level += 1
        Response(mob, "you gained a level!")
    }
}
