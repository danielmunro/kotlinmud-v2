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

    fun getHealthIndication(): String {
        val amount = hp.toDouble() / calc(Attribute.Hp).toDouble()
        return when {
            amount == 1.0 -> "$name is in excellent condition."
            amount > 0.9 -> "$name has a few scratches."
            amount > 0.75 -> "$name has some small wounds and bruises."
            amount > 0.5 -> "$name has quite a few wounds."
            amount > 0.3 -> "$name has some big nasty wounds and scratches."
            amount > 0.15 -> "$name looks pretty hurt."
            else -> "$name is in awful condition."
        }
    }
}
