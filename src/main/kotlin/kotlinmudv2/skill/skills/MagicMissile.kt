package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createMagicMissileSkill(): Skill {
    return Skill(
        SkillName.MagicMissile,
        listOf(Role.Mage),
        1,
        listOf(
            Pair(Cost.Mana, 50),
            Pair(Cost.Delay, 1),
        ),
        listOf(
            "you lose your concentration",
            "%s loses their concentration",
            "%s loses their concentration",
        ),
        true,
        { request ->
            val amount = (request.mob.attributes[Attribute.Int] ?: 0) - (request.mob.target?.attributes?.get(Attribute.Int) ?: 0)
            d20() > 5 - amount
        },
        { request, level ->
            val amount = level * 5
//            request.mob.target?.hp = (request.mob.target?.hp ?: 0) - amount
//            actionService.damageReceived(mob, mob.target!!)
            request.respondToRoomWithTarget(
                "you cast 'magic missile', giving %s a glancing blow",
                "%s casts 'magic missile', giving %s a glancing blow",
                request.mob.target!!,
                "%s casts 'magic missile', giving you a glancing blow",
            )
        },
    )
}
