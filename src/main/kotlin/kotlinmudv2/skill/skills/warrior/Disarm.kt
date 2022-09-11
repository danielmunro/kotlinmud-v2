package kotlinmudv2.skill.skills.warrior

import kotlinmudv2.game.Attribute
import kotlinmudv2.item.Position
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.Role
import kotlinmudv2.skill.Cost
import kotlinmudv2.skill.Skill
import kotlinmudv2.skill.SkillName

fun createDisarmSkill(): Skill {
    return Skill(
        SkillName.Disarm,
        listOf(Role.Warrior),
        1,
        listOf(
            Pair(Cost.Moves, 20),
            Pair(Cost.Delay, 1),
        ),
        listOf(
            "you fail to disarm %s!",
            "%s tries to disarm %s but fails.",
            "%s tries to disarm you but fails.",
        ),
        true,
        { request -> request.mob.rollForAttribute(Attribute.Str) },
    ) { request, anyTarget, level ->
        val target = anyTarget as Mob
        target.equipped.find { it.position == Position.Weapon }?.let {
            target.equipped.remove(it)
            request.getRoom()?.items?.add(it)
            return@Skill request.respondToRoomWithTarget(
                "you disarm $target and send their weapon flying!",
                "${request.mob} disarms $target and sends their weapon flying!",
                target,
                "${request.mob} disarms you and sends your weapon flying!",
            )
        }
        request.respond("they do not have a weapon equipped!")
    }
}
