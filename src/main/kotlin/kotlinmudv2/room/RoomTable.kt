package kotlinmudv2.room

import org.jetbrains.exposed.dao.id.IntIdTable

object RoomTable : IntIdTable() {
    val name = varchar("name", 255)
    val description = text("description")
    val exits = text("exits")
}
