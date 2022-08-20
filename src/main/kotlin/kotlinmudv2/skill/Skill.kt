package kotlinmudv2.skill

import kotlinmudv2.action.ActionService
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role

class Skill(
    val name: SkillName,
    val roles: List<Role>,
    val level: Int,
    val costs: List<Pair<Cost, Int>>,
    val success: String,
    val failure: String,
    val rollCheck: (ActionService, Mob) -> Boolean,
    val execute: (ActionService, Mob, Int) -> Unit,
) {
    fun canApplyCosts(mob: Mob): Boolean {
        return costs.all {
            when (it.first) {
                Cost.Delay -> true
                Cost.Hp -> mob.hp > it.second
                Cost.Mana -> mob.mana > it.second
                Cost.Moves -> mob.moves > it.second
            }
        }
    }

    fun applyCosts(mob: Mob) {
        costs.forEach {
            when (it.first) {
                Cost.Delay -> null
                Cost.Hp -> mob.hp -= it.second
                Cost.Mana -> mob.mana -= it.second
                Cost.Moves -> mob.moves -= it.second
            }
        }
    }
}