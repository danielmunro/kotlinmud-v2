package kotlinmudv2.skill.skills.cleric

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlinmudv2.skill.Spell
import kotlinmudv2.socket.RoomMessage

fun createHealSkill(): Skill {
    return Spell(
        SkillName.Heal,
        listOf(Role.Cleric),
        1,
        listOf(
            Pair(Cost.Mana, 100),
            Pair(Cost.Delay, 1),
        ),
        false,
        { request -> request.mob.rollForAttribute(Attribute.Wis) },
        { request, anyTarget, level ->
            val target = anyTarget as Mob
            val amount = level * 15
            target.hp += amount
            target.calc(Attribute.Hp).also {
                if (it > target.hp) {
                    target.hp = it
                }
            }
            request.sendToRoom(
                RoomMessage(
                    request.mob,
                    "you cast 'heal'",
                    "${request.mob} casts 'heal'",
                )
            )
            request.respondToRoomWithTarget(
                "$target feels better.",
                "$target feels better.",
                target,
                "you feel better!",
            )
        },
    )
}
