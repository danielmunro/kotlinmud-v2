package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Disposition
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus

fun createOpenAction(): Action {
    return Action(
        Command.Open,
        listOf(Syntax.Command, Syntax.Door),
        listOf(
            Disposition.Standing,
            Disposition.Fighting,
        ),
    ) { _, mob, context, _ ->
        val exit = context[1] as Exit
        if (exit.status == ExitStatus.Locked) {
            return@Action Response(
                mob,
                "the ${exit.keyword} is locked"
            )
        }
        if (exit.status == ExitStatus.Open) {
            return@Action Response(
                mob,
                "the ${exit.keyword} is already open"
            )
        }
        exit.status = ExitStatus.Open
        Response(
            mob,
            "you open the ${exit.keyword}",
        )
    }
}
