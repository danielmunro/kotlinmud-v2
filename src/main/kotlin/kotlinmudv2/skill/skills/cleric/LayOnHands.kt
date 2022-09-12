package kotlinmudv2.skill.skills.cleric

import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName
import kotlinmudv2.skill.Spell
import kotlinmudv2.socket.RoomMessage

fun createLayOnHandsSkill(): Skill {
    return Spell(
        SkillName.LayOnHands,
        Role.Cleric,
        1,
        listOf(
            Pair(Cost.Mana, 100),
            Pair(Cost.Delay, 1),
        ),
        false,
        { request -> request.mob.rollForAttribute(Attribute.Wis) },
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        val amount = Math.max(level * 30, request.mob.hp - 20)
        request.mob.hp -= amount
        target.hp += amount
        target.calc(Attribute.Hp).also {
            if (it > target.hp) {
                target.hp = it
            }
        }
        request.sendToRoom(
            RoomMessage(
                request.mob,
                "you cast 'lay on hands'",
                "${request.mob} casts 'lay on hands'",
            )
        )
        request.respondToRoomWithTarget(
            "you lay hands on $target, boosting their health.",
            "${request.mob} lays hands on $target, boosting their health.",
            target,
            "${request.mob} lays hands on you, boosting your health.",
        )
    }
}
