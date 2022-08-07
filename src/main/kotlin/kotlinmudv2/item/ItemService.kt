package kotlinmudv2.item

import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception

class ItemService {
    private val items = mutableListOf<Item>()

    fun clone(item: Item): Item {
        return transaction { ItemEntity.findById(item.id) }?.let {
            createFromEntity(it)
        } ?: throw Exception("item entity not found for model")
    }

    fun createFromEntity(entity: ItemEntity): Item {
        return mapItem(entity).also {
            items.add(it)
        }
    }

    fun mapItem(entity: ItemEntity): Item {
        return Item(
            entity.id.value,
            entity.name,
            entity.description,
            entity.brief,
            ItemType.valueOf(entity.itemType),
            entity.material,
            entity.level,
            entity.value,
            entity.flags,
            entity.attributes,
            entity.affects,
            entity.position?.let { Position.valueOf(it) }
        )
    }

    fun affectDecay() {
        items.forEach { item ->
            item.affects.entries.removeIf {
                if (it.value == 0) {
                    return@removeIf true
                }
                if (it.value > 0) {
                    it.setValue(it.value - 1)
                }
                false
            }
        }
    }
}
