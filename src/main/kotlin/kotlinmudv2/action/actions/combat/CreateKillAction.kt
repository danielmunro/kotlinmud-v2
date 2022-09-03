package kotlinmudv2.action.actions.combat

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.alertDisposition

fun createKillAction(): Action {
    return Action(
        Command.Kill,
        listOf(Syntax.Command, Syntax.MobInRoom),
        alertDisposition(),
    ) { request ->
        (request.context[1] as Mob).let {
            request.mob.target = it
            request.respondToRoomWithTarget(
                "you scream and attack!",
                "${request.mob} screams and attacks $it!",
                it,
                "${request.mob} screams and attacks you!"
            )
        }
    }
}
