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
    maxHp: Int,
    mana: Int,
    maxMana: Int,
    moves: Int,
    maxMoves: Int,
    roomId: Int,
) : Mob(id, name, brief, description, race, items, hp, maxHp, mana, maxMana, moves, maxMoves, roomId)
