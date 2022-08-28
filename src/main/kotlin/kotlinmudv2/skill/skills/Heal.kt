package kotlinmudv2.skill.skills

import kotlinmudv2.dice.d20
import kotlinmudv2.game.Attribute
import kotlinmudv2.mob.Mob
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
        { request, anyTarget, level ->
            val target = anyTarget as Mob
            val amount = level * 15
            target.hp += amount
            target.calc(Attribute.Hp).also {
                if (it > target.hp) {
                    target.hp = it
                }
            }
            request.respondToRoomWithTarget(
                "you cast 'heal', %s feels better.",
                "%s casts 'heal', %s feels better.",
                target,
                "%s casts 'heal', you feel better!",
            )
        },
    )
}
