package kotlinmudv2.room

import kotlinmudv2.item.Item

class NewRoom(
    val name: String,
    val description: String,
    val items: MutableList<Item>,
    val northId: Int?,
    val southId: Int?,
    val eastId: Int?,
    val westId: Int?,
    val upId: Int?,
    val downId: Int?,
)
