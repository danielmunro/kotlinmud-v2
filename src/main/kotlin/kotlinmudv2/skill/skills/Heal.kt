package kotlinmudv2.skill.skills

import kotlinmudv2.action.Response
import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createHealSkill(): Skill {
    return Skill(
        SkillName.Heal,
        listOf(Role.Cleric),
        1,
        listOf(
            Pair(Cost.Mana, 100),
            Pair(Cost.Delay, 1),
        ),
        "you feel better!",
        "you lose your concentration",
        false,
        { _, mob ->
            d20() > 5 - ((mob.attributes[Attribute.Wis] ?: 0) / 5)
        },
        { _, mob, level ->
            val amount = level * 15
            mob.hp += amount
            mob.calc(Attribute.Hp).also {
                if (it > mob.hp) {
                    mob.hp = it
                }
            }
            Response(
                mob,
                "you heal foo"
            )
        },
    )
}
