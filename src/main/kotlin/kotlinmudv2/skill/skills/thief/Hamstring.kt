package kotlinmudv2.skill.skills.thief

import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlin.random.Random

fun createHamstringSkill(): Skill {
    return Skill(
        SkillName.Hamstring,
        Role.Thief,
        1,
        listOf(
            Pair(Cost.Moves, 100),
            Pair(Cost.Delay, 2),
        ),
        listOf(
            "your hamstring misses %s harmlessly",
            "%s's hamstring misses %s harmlessly",
            "%s's hamstring misses you harmlessly",
        ),
        true,
        { request -> request.mob.rollForAttribute(Attribute.Dex) }
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        val amount = Random.nextInt(level - 5, level).coerceAtLeast(1)
        request.doHit(
            Hit(
                request.mob,
                target,
                amount,
                request.mob.damageType(),
            )
        )
        target.affects.add(
            Affect(
                AffectType.Hamstrung,
                1,
            )
        )
        request.respondToRoomWithTarget(
            "you hamstring $target, making them limp.",
            "${request.mob} hamstrings $target, making them limp.",
            target,
            "${request.mob} hamstrings you, making you limp.",
        )
    }
}
