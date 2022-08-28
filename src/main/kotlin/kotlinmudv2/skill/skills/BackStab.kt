package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Hit
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
        { request ->
            val dexDiff = (request.mob.target?.attributes?.get(Attribute.Dex) ?: 0) - (request.mob.attributes[Attribute.Dex] ?: 0)
            d20() > 5 + dexDiff
        }
    ) { request, level ->
        request.mob.target?.let { target ->
            val amount = Random.nextInt(level - 5, level + 5)
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
        } ?: request.respondError("you don't have a target")
    }
}
