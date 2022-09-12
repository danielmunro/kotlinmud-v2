package kotlinmudv2.skill.skills.mage

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

fun createEnfeebleSkill(): Skill {
    return Spell(
        SkillName.Fireball,
        Role.Mage,
        1,
        listOf(
            Pair(Cost.Mana, 100),
            Pair(Cost.Delay, 1),
        ),
        true,
        { request -> request.mob.rollForAttribute(Attribute.Int) },
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        val amount = (level / 5).coerceAtLeast(1).coerceAtMost(3)
        target.affects.add(
            Affect(
                AffectType.Enfeeble,
                1,
                mapOf(Pair(Attribute.Str, -amount)),
            )
        )
        request.sendToRoom(
            RoomMessage(
                request.mob,
                "you cast 'enfeeble'",
                "${request.mob} casts 'enfeeble'",
                target,
                "${request.mob} casts 'enfeeble'",
            )
        )
        request.respondToRoomWithTarget(
            "$target is suddenly enfeebled with great weakness",
            "$target is suddenly enfeebled with great weakness",
            target,
            "you are suddenly enfeebled with great weakness",
        )
    }
}
