package kotlinmudv2.room

import kotlinmudv2.item.Item

class Room (
    val id: Int,
    val name: String,
    val description: String,
    val items: List<Item>,
    val northId: Int?,
    val southId: Int?,
    val eastId: Int?,
    val westId: Int?,
    val upId: Int?,
    val downId: Int?,
)