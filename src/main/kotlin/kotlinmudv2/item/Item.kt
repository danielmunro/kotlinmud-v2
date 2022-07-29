package kotlinmudv2.item

import kotlinmudv2.game.Attribute

class Item(
    val id: Int,
    val name: String,
    val description: String,
    val brief: String,
    val itemType: ItemType,
    val attributes: MutableMap<Attribute, Int>,
    val position: Position? = null,
)
