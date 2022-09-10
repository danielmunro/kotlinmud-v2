package kotlinmudv2.skill.skills.warrior

import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createDirtKickSkill(): Skill {
    return Skill(
        SkillName.DirtKick,
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
        { request -> request.mob.rollForAttribute(Attribute.Str) },
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        target.affects.add(
            Affect(
                AffectType.Blind,
                (level / 4).coerceAtLeast(1)
            )
        )
        request.respondToRoomWithTarget(
            "you kick dirt in $target's eyes, blinding them.",
            "${request.mob} kicks dirt in $target's eyes, blinding them.",
            target,
            "${request.mob} kicks dirt in your eyes, you are blinded!",
        )
    }
}
