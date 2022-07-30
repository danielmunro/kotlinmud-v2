package kotlinmudv2.room

import org.jetbrains.exposed.dao.id.IntIdTable

object RoomTable : IntIdTable() {
    val name = varchar("name", 255)
    val description = text("description")
    val northId = integer("northId").nullable()
    val southId = integer("southId").nullable()
    val eastId = integer("eastId").nullable()
    val westId = integer("westId").nullable()
    val upId = integer("upId").nullable()
    val downId = integer("downId").nullable()
}
