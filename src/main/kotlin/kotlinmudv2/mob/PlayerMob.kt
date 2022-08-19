package kotlinmudv2.mob

import kotlinmudv2.game.Affect
import kotlinmudv2.game.Attribute
import kotlinmudv2.item.Item
import kotlinmudv2.skill.SkillName

class PlayerMob(
    val password: ByteArray,
    val salt: ByteArray,
    var experience: Int,
    var experiencePerLevel: Int,
    val role: Role,
    id: Int,
    name: String,
    brief: String,
    description: String,
    race: RaceType,
    level: Int,
    items: MutableList<Item>,
    equipped: MutableList<Item>,
    attributes: MutableMap<Attribute, Int>,
    affects: MutableList<Affect>,
    hp: Int,
    mana: Int,
    moves: Int,
    roomId: Int,
    coins: Int,
    disposition: Disposition,
    flags: List<MobFlag>,
    skills: MutableMap<SkillName, Int>,
) : Mob(
    id,
    name,
    brief,
    description,
    race,
    level,
    items,
    equipped,
    attributes,
    affects,
    hp,
    mana,
    moves,
    roomId,
    coins,
    disposition,
    flags,
    skills,
) {
    var debitLevel = false
}
