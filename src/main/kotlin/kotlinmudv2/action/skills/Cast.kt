package kotlinmudv2.action.skills

import kotlinmudv2.action.Action
import kotlinmudv2.action.Command
import kotlinmudv2.action.SkillContext
import kotlinmudv2.action.Syntax
import kotlinmudv2.action.trySkill
import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.Mob

fun createCastAction(): Action {
    return Action(
        Command.Cast,
        listOf(Syntax.Command, Syntax.Skill, Syntax.OptionalTarget),
        listOf(Disposition.Fighting, Disposition.Standing),
    ) { actionService, mob, context, _ ->
        val ctx = context[1] as SkillContext
        trySkill(actionService, mob, ctx.skill, ctx.level, context[2] as Mob?)
    }
}
