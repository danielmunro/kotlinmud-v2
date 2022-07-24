package kotlinmudv2.room

import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoomEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomEntity>(RoomTable)
    var name by RoomTable.name
    var description by RoomTable.description
    var northId by RoomTable.northId
    var southId by RoomTable.southId
    var eastId by RoomTable.eastId
    var westId by RoomTable.westId
    var upId by RoomTable.upId
    var downId by RoomTable.downId
    val items by ItemEntity optionalReferrersOn ItemTable.room
}
