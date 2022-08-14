package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.Exit

fun createCloseExitAction(): Action {
    return Action(
        Command.Close,
        listOf(Syntax.Command, Syntax.Door),
        listOf(
            Disposition.Standing,
            Disposition.Fighting,
        ),
    ) { actionService, mob, context, _ ->
        close(actionService, context[1] as Exit, mob)
    }
}
