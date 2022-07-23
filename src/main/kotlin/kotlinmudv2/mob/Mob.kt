package kotlinmudv2.mob

import kotlinmudv2.item.Item

class Mob(
    val id: Int,
    val name: String,
    val description: String,
    val items: List<Item>,
    var hp: Int,
    var maxHp: Int,
    var mana: Int,
    var maxMana: Int,
    var moves: Int,
    var maxMoves: Int,
    var roomId: Int,
)