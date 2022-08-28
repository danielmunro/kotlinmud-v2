package kotlinmudv2.skill.skills

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
        listOf(
            "you lose your concentration",
            "%s loses their concentration",
            "%s loses their concentration",
        ),
        false,
        { request ->
            d20() > 5 - ((request.mob.attributes[Attribute.Wis] ?: 0) / 5)
        },
        { request, level ->
            val amount = level * 15
            request.mob.hp += amount
            request.mob.calc(Attribute.Hp).also {
                if (it > request.mob.hp) {
                    request.mob.hp = it
                }
            }
            request.respondToRoom(
                "you cast 'heal', %s feels better.",
                "%s casts 'heal', %s feels better.",
            )
        },
    )
}
