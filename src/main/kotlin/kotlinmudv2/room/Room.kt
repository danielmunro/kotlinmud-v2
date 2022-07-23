package kotlinmudv2.room

import kotlinmudv2.item.Item

class Room (
    val id: Int,
    val name: String,
    val description: String,
    val items: List<Item>,
    var northId: Int?,
    var southId: Int?,
    var eastId: Int?,
    var westId: Int?,
    var upId: Int?,
    var downId: Int?,
)