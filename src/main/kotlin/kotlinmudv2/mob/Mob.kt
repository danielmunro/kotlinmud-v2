package kotlinmudv2.mob

import kotlinmudv2.game.Attribute
import kotlinmudv2.item.Item

open class Mob(
    val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val race: Race,
    val items: MutableList<Item>,
    var hp: Int,
    var mana: Int,
    var moves: Int,
    var roomId: Int,
) {
    var target: Mob? = null
    private val attributes = mutableMapOf<Attribute, Int>()

    fun calc(attribute: Attribute): Int {
        return (attributes[attribute] ?: 0) + (raceAttributes[race]?.get(attribute) ?: 0)
    }
}
