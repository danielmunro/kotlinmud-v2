package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlin.random.Random

fun createBashSkill(): Skill {
    return Skill(
        SkillName.Bash,
        listOf(Role.Warrior),
        1,
        listOf(
            Pair(Cost.Moves, 20),
            Pair(Cost.Delay, 2),
        ),
        listOf(
            "you fall flat on your face!",
            "%s tries to bash %s and falls flat on their face!",
            "%s tries to bash you and falls flat on their face!",
        ),
        true,
        { request ->
            val sizeDiff = (request.mob.target?.race?.size?.value ?: 0) - request.mob.race.size.value
            d20() <= 5 + sizeDiff
        },
    ) { request, level ->
        request.mob.target?.let { target ->
            val initial = (level / 3).coerceAtLeast(1)
            val affect = target.affects.find { it.type == AffectType.Stun }
            val impact = (level / 5).coerceAtLeast(1)
            if (affect == null) {
                target.affects.add(
                    Affect(
                        AffectType.Stun,
                        initial,
                        mutableMapOf(
                            Pair(Attribute.Int, -impact),
                        ),
                    )
                )
            } else {
                affect.timeout = (affect.timeout + initial).coerceAtMost(4)
            }
            val amount = Random.nextInt(initial - 1, initial + 1).coerceAtLeast(1)
            request.doHit(
                Hit(
                    request.mob,
                    target,
                    amount,
                    request.mob.damageType(),
                )
            )
            request.respondToRoomWithTarget(
                "you slam into $target and send them flying!",
                "${request.mob} slams into $target and sends them flying!",
                target,
                "${request.mob} slams into you and sends you flying!",
            )
        } ?: request.respondError("you don't have a target")
    }
}
