package kotlinmudv2.skill.skills.thief

import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createSneakSkill(): Skill {
    return Skill(
        SkillName.Sneak,
        Role.Thief,
        1,
        listOf(
            Pair(Cost.Moves, 20),
            Pair(Cost.Delay, 2),
        ),
        listOf(
            "you fail to sneak",
            "%s fails to sneak",
            "%s fails to sneak",
        ),
        true,
        { request -> request.mob.rollForAttribute(Attribute.Dex) }
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        val amount = (level * 1.25).toInt().coerceAtLeast(5).coerceAtMost(25)
        target.noStackAddAffect(
            Affect(
                AffectType.Sneak,
                amount,
            )
        )
        request.respond(
            "you start sneaking silently",
        )
    }
}
