package kotlinmudv2.action.actions

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.Syntax
import kotlinmudv2.mob.Mob

fun createKillAction(): Action {
    return Action(
        Command.Kill,
        listOf(Syntax.Command, Syntax.MobInRoom),
    ) { _, mob, context, _ ->
        (context[1] as Mob).let {
            mob.target = it
            Response(mob, "you scream and attack!")
        }
    }
}
