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

fun createFireballSkill(): Skill {
    return Spell(
        SkillName.Fireball,
        Role.Mage,
        1,
        listOf(
            Pair(Cost.Mana, 50),
            Pair(Cost.Delay, 1),
        ),
        true,
        { request -> request.mob.rollForAttribute(Attribute.Int) },
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        val amount = level * 9
        request.doHit(
            Hit(
                request.mob,
                target,
                amount,
                DamageType.Fire,
            )
        )
        request.sendToRoom(
            RoomMessage(
                request.mob,
                "you cast 'fireball'",
                "${request.mob} casts 'fireball'",
                target,
                "${request.mob} casts 'fireball'",
            )
        )
        request.respondToRoomWithTarget(
            "a ball of fire shoots from your hands, giving $target a glancing blow",
            "a ball of fire shoots from ${request.mob}'s hands, giving $target a glancing blow",
            target,
            "a ball of fire shoots from ${request.mob}'s hands, giving you a glancing blow",
        )
    }
}
