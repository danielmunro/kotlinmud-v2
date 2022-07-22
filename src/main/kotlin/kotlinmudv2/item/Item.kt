package kotlinmudv2.item

class Item (entity: ItemEntity) {
    val name = entity.name
    val description = entity.description
    val itemType = ItemType.valueOf(entity.itemType)
}
