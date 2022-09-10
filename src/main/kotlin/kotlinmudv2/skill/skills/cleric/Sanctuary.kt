package kotlinmudv2.skill.skills.cleric

import kotlinmudv2.game.Affect
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlinmudv2.skill.Spell
import kotlinmudv2.socket.RoomMessage

fun createSanctuarySkill(): Skill {
    return Spell(
        SkillName.Sanctuary,
        listOf(Role.Cleric),
        10,
        listOf(
            Pair(Cost.Mana, 120),
            Pair(Cost.Delay, 1),
        ),
        false,
        { request -> request.mob.rollForAttribute(Attribute.Wis) },
        { request, anyTarget, level ->
            val target = anyTarget as Mob
            val timeout = (level / 3).coerceAtLeast(1).coerceAtMost(8)
            target.affects.add(Affect(AffectType.Sanctuary, timeout))
            request.sendToRoom(
                RoomMessage(
                    request.mob,
                    "you cast 'sanctuary'",
                    "${request.mob} casts 'sanctuary'",
                )
            )
            request.respondToRoomWithTarget(
                "$target is surrounded by a bright aura.",
                "$target is surrounded by a bright aura.",
                target,
                "you are surrounded by a bright aura.",
            )
        },
    )
}