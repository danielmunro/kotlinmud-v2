package kotlinmudv2.item

class ItemService {
    private val items = mutableListOf<Item>()

    fun createFromEntity(entity: ItemEntity): Item {
        return Item(
            entity.id.value,
            entity.name,
            entity.description,
            entity.brief,
            ItemType.valueOf(entity.itemType),
        ).also {
            items.add(it)
        }
    }
}
