package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.Exit

fun createOpenDirectionAction(): Action {
    return Action(
        Command.Open,
        listOf(Syntax.Command, Syntax.Direction),
        listOf(Disposition.Standing, Disposition.Fighting),
    ) { actionService, mob, context, _ ->
        open(actionService, context[1] as Exit, mob)
    }
}
