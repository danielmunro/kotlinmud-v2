package kotlinmudv2.room

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Room(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Room>(RoomTable)
    var name by RoomTable.name
    var description by RoomTable.description
    var northId by RoomTable.northId
    var southId by RoomTable.southId
    var eastId by RoomTable.eastId
    var westId by RoomTable.westId
    var upId by RoomTable.upId
    var downId by RoomTable.downId
}