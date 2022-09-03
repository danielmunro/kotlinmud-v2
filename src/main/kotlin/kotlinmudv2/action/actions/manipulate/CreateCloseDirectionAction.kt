package kotlinmudv2.action.actions.manipulate

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.Exit

fun createCloseDirectionAction(): Action {
    return Action(
        Command.Close,
        listOf(Syntax.Command, Syntax.Direction),
        listOf(
            Disposition.Standing,
            Disposition.Fighting,
        ),
    ) { request ->
        close(request, request.context[1] as Exit)
    }
}
