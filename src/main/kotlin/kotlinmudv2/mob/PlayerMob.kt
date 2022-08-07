package kotlinmudv2.mob

import kotlinmudv2.game.Affect
import kotlinmudv2.game.Attribute
import kotlinmudv2.item.Item

class PlayerMob(
    val password: ByteArray,
    val salt: ByteArray,
    var worth: Int,
    id: Int,
    name: String,
    brief: String,
    description: String,
    race: Race,
    level: Int,
    items: MutableList<Item>,
    equipped: MutableList<Item>,
    attributes: MutableMap<Attribute, Int>,
    affects: MutableMap<Affect, Int>,
    hp: Int,
    mana: Int,
    moves: Int,
    roomId: Int,
    coins: Int,
    disposition: Disposition,
    flags: List<MobFlag>,
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
)
