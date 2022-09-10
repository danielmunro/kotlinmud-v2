package kotlinmudv2.skill.skills.mage

import kotlinmudv2.game.Attribute
import kotlinmudv2.game.DamageType
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlinmudv2.skill.Spell
import kotlinmudv2.socket.RoomMessage

fun createMagicMissileSkill(): Skill {
    return Spell(
        SkillName.MagicMissile,
        listOf(Role.Mage),
        1,
        listOf(
            Pair(Cost.Mana, 50),
            Pair(Cost.Delay, 1),
        ),
        true,
        { request -> request.mob.rollForAttribute(Attribute.Int) },
        { request, anyTarget, level ->
            val target = anyTarget as Mob
            val amount = level * 5
            request.doHit(
                Hit(
                    request.mob,
                    target,
                    amount,
                    DamageType.Energy,
                )
            )
            request.sendToRoom(
                RoomMessage(
                    request.mob,
                    "you cast 'magic missile'",
                    "${request.mob} casts 'magic missile'",
                    target,
                    "${request.mob} casts 'magic missile'",
                )
            )
            request.respondToRoomWithTarget(
                "a bolt of magic leaps from your hand, leaving $target a glancing blow",
                "a bolt of magic leaps from ${request.mob}'s hand, leaving $target a glancing blow",
                target,
                "a bolt of magic leaps from ${request.mob}'s hand, leaving you a glancing blow",
            )
        },
    )
}
