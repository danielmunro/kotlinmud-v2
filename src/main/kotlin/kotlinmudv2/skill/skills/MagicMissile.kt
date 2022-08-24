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
        "your magic missile grazes %s",
        "you lose your concentration",
        true,
        { _, mob ->
            val amount = (mob.attributes[Attribute.Int] ?: 0) - (mob.target?.attributes?.get(Attribute.Int) ?: 0)
            d20() > 5 - amount
        },
        { actionService, mob, level ->
            val amount = level * 5
            mob.target?.hp = (mob.target?.hp ?: 0) - amount
            actionService.damageReceived(mob, mob.target!!)
        },
    )
}
