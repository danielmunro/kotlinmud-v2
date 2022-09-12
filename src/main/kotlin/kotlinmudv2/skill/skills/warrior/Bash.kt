package kotlinmudv2.skill.skills.warrior

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

fun createBashSkill(): Skill {
    return Skill(
        SkillName.Bash,
        Role.Warrior,
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
        { request -> request.mob.rollForAttribute(Attribute.Str) },
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        val initial = (level / 3).coerceAtLeast(1)
        val amount = Random.nextInt(initial - 1, initial + 1).coerceAtLeast(1)
        target.affects.add(Affect(AffectType.Stun, 1))
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
    }
}
