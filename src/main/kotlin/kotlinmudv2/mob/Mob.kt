package kotlinmudv2.mob

import kotlinmudv2.item.Item

open class Mob(
    val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val items: MutableList<Item>,
    var hp: Int,
    var maxHp: Int,
    var mana: Int,
    var maxMana: Int,
    var moves: Int,
    var maxMoves: Int,
    var roomId: Int,
) {
    var target: Mob? = null
}
