package kotlinmudv2.room

import kotlinmudv2.item.Item

const val startRoomId = 3001

class Room(
    val id: Int,
    val name: String,
    val description: String,
    val items: MutableList<Item>,
    var exits: List<Exit>,
)
