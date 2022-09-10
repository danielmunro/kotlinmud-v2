package kotlinmudv2.skill.skills.thief

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlin.random.Random

fun createBackStabSkill(): Skill {
    return Skill(
        SkillName.BackStab,
        listOf(Role.Thief),
        1,
        listOf(
            Pair(Cost.Moves, 100),
            Pair(Cost.Delay, 2),
        ),
        listOf(
            "your backstab misses %s harmlessly",
            "%s's backstab misses %s harmlessly",
            "%s's backstab misses you harmlessly",
        ),
        true,
        { request -> request.mob.rollForAttribute(Attribute.Dex) }
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        val amount = Random.nextInt(level - 5, level + 5).coerceAtLeast(1)
        request.doHit(
            Hit(
                request.mob,
                target,
                amount,
                request.mob.damageType(),
            )
        )
        request.respondToRoomWithTarget(
            "you stab $target in the back, making them gasp in pain!",
            "${request.mob} stabs $target in the back, making them gasp in pain!",
            target,
            "${request.mob} stabs you in the back!"
        )
    }
}
