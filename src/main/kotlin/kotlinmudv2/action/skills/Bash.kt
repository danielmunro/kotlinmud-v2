package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.SkillContext
import kotlinmudv2.action.Syntax
import kotlinmudv2.dice.d4
import kotlinmudv2.mob.Disposition

fun createBashSkill(): Action {
    return Action(
        Command.Bash,
        listOf(Syntax.Skill),
        listOf(Disposition.Fighting),
    ) { actionService, mob, context, input ->
        val skill = context[1] as SkillContext
        if (d4() == 1) {
            return@Action Response(
                mob,
                "you fall flat on your face!"
            )
        }
        Response(
            mob,
            "",
        )
    }
}
