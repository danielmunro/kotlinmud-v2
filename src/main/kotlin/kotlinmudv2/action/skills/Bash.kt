package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Command
import kotlinmudv2.action.Response
import kotlinmudv2.action.SkillContext
import kotlinmudv2.action.Syntax
import kotlinmudv2.dice.d4
import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Disposition

fun createBashAction(): Action {
    return Action(
        Command.Bash,
        listOf(Syntax.Skill),
        listOf(Disposition.Fighting),
    ) { actionService, mob, context, _ ->
        val ctx = context[0] as SkillContext
        if (!actionService.applySkillCosts(mob, ctx)) {
            return@Action Response(
                mob,
                "you are too tired and cannot do that",
            )
        }
        if (d4() == 1) {
            return@Action Response(
                mob,
                "you fall flat on your face!",
                ActionStatus.Failure,
            )
        }
        mob.target?.also { target ->
            val amount = (ctx.level / 3).coerceAtLeast(1)
            val affect = target.affects.find { it.type == AffectType.Stun }
            if (affect == null) {
                target.affects.add(
                    Affect(
                        AffectType.Stun,
                        amount,
                        mutableMapOf(
                            Pair(Attribute.Int, -2),
                        ),
                    )
                )
            } else {
                affect.timeout += amount
            }
        }
        Response(
            mob,
            "you slam into ${mob.target!!.name} and send them flying!",
        )
    }
}
