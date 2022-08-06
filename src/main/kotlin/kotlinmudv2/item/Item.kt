package kotlinmudv2.item

import kotlinmudv2.game.Affect
import kotlinmudv2.game.Attribute

class Item(
    val id: Int,
    val name: String,
    val description: String,
    val brief: String,
    val itemType: ItemType,
    val material: String,
    val level: Int,
    val value: Int,
    val attributes: MutableMap<Attribute, Int>,
    val affects: MutableMap<Affect, Int>,
    val position: Position? = null,
)
