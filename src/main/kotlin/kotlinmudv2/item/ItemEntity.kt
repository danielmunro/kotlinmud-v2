package kotlinmudv2.item

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemEntity>(ItemTable)
    var name by ItemTable.name
    var brief by ItemTable.brief
    var description by ItemTable.description
    var itemType by ItemTable.itemType
    var mobInventory by ItemTable.mobInventory
    var mobEquipped by ItemTable.mobEquipped
    var room by ItemTable.room
}
