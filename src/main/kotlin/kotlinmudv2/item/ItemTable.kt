package kotlinmudv2.item

import kotlinmudv2.mob.MobTable
import kotlinmudv2.room.RoomTable
import org.jetbrains.exposed.dao.id.IntIdTable

object ItemTable : IntIdTable() {
    val name = varchar("name", 50)
    val brief = varchar("brief", 50)
    val description = varchar("description", 255)
    val itemType = varchar("itemType", 64)
    val mob = reference("mobId", MobTable)
    val room = reference("roomId", RoomTable)
}