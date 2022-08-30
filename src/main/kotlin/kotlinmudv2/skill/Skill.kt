package kotlinmudv2.skill

import kotlinmudv2.action.Request
import kotlinmudv2.action.Response
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role

class Skill(
    val name: SkillName,
    val roles: List<Role>,
    val level: Int,
    val costs: List<Pair<Cost, Int>>,
    val failureMessages: List<String>,
    val isOffensive: Boolean,
    val rollCheck: (Request) -> Boolean,
    val execute: (Request, target: Any, Int) -> Response,
) {
    fun canApplyCosts(mob: Mob): Boolean {
        return costs.all {
            when (it.first) {
                Cost.Delay -> true
                Cost.Hp -> mob.hp > it.second
                Cost.Mana -> mob.mana >= it.second
                Cost.Moves -> mob.moves >= it.second
            }
        }
    }

    fun applyCosts(mob: Mob) {
        costs.forEach {
            when (it.first) {
                Cost.Delay -> {}
                Cost.Hp -> mob.hp -= it.second
                Cost.Mana -> mob.mana -= it.second
                Cost.Moves -> mob.moves -= it.second
            }
        }
    }
}
