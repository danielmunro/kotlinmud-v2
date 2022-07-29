package kotlinmudv2.mob

import kotlinmudv2.game.Attribute
import kotlinmudv2.item.Item

class PlayerMob(
    val password: ByteArray,
    val salt: ByteArray,
    id: Int,
    name: String,
    brief: String,
    description: String,
    race: Race,
    items: MutableList<Item>,
    equipped: MutableList<Item>,
    attributes: MutableMap<Attribute, Int>,
    hp: Int,
    mana: Int,
    moves: Int,
    roomId: Int,
) : Mob(id, name, brief, description, race, items, equipped, attributes, hp, mana, moves, roomId)
