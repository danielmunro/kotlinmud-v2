package kotlinmudv2.room

import kotlinmudv2.item.Item

class NewRoom(
    val name: String,
    val description: String,
    val items: MutableList<Item>,
    val exits: MutableList<Exit>,
)
