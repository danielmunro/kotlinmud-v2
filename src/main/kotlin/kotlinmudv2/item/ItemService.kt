package kotlinmudv2.item

class ItemService {
    private val items = mutableListOf<Item>()

    fun createFromEntity(entity: ItemEntity): Item {
        return Item(
            entity.id.value,
            entity.name,
            entity.brief,
            entity.description,
            ItemType.valueOf(entity.itemType),
        )
    }
}