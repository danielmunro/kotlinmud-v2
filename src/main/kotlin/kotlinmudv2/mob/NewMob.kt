package kotlinmudv2.mob

import kotlinmudv2.item.Item

class NewMob(
    val name: String,
    val brief: String,
    val description: String,
    val race: Race,
    val items: MutableList<Item>,
    var hp: String,
    var mana: String,
    var moves: String,
    var roomId: Int,
)
