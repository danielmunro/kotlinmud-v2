package kotlinmudv2.action

import kotlinmudv2.mob.Mob
import kotlinmudv2.skill.Skill

fun doTargeting(mob: Mob, skill: Skill, specifiedTarget: Mob?): Boolean {
    val target = if (specifiedTarget != null) {
        specifiedTarget
    } else if (!skill.isOffensive) {
        mob
    } else {
        mob.target
    }
    if (mob.target != null && specifiedTarget != null && mob.target != specifiedTarget) {
        return false
    }
    if (mob.target == null && target != mob && skill.isOffensive) {
        mob.target = target
    }
    return true
}