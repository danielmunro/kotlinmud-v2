package kotlinmudv2.item

class NewItem(
    val name: String,
    val description: String,
    val brief: String,
    val itemType: ItemType,
    val mobId: Int?,
    val roomId: Int?,
)
