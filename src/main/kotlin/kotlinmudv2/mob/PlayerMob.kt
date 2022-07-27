package kotlinmudv2.mob

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
    hp: Int,
    mana: Int,
    moves: Int,
    roomId: Int,
) : Mob(id, name, brief, description, race, items, hp, mana, moves, roomId)
