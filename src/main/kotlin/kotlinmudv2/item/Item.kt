package kotlinmudv2.item

class Item(
    val id: Int,
    val name: String,
    val description: String,
    val brief: String,
    val itemType: ItemType,
    val position: Position? = null,
)
