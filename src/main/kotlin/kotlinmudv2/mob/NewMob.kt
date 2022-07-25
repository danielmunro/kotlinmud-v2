package kotlinmudv2.mob

import kotlinmudv2.item.Item

class NewMob(
    val name: String,
    val brief: String,
    val description: String,
    val items: MutableList<Item>,
    var hp: Int,
    var mana: Int,
    var moves: Int,
    var roomId: Int,
)
