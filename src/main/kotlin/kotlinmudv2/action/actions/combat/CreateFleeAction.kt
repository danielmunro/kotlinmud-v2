package kotlinmudv2.action.actions.combat

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition

fun createFleeAction(): Action {
    return Action(
        Command.Flee,
        listOf(Syntax.Command),
        listOf(Disposition.Fighting),
    ) { request ->
        request.getRoom()?.exits?.randomOrNull()?.let {
            request.moveMob(it.direction)
            request.respondToRoom(
                "you flee running scared!",
                "${request.mob} flees running scared!",
            )
        } ?: request.respondError("you have nowhere to flee!")
    }
}
