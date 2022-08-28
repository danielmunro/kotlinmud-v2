package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.Exit

fun createOpenExitAction(): Action {
    return Action(
        Command.Open,
        listOf(Syntax.Command, Syntax.Door),
        listOf(
            Disposition.Standing,
            Disposition.Fighting,
        ),
    ) { request ->
        open(request, request.context[1] as Exit)
    }
}
