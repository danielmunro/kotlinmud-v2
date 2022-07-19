package kotlinmudv2.room

import org.jetbrains.exposed.dao.id.IntIdTable

object RoomTable : IntIdTable() {
    val name = varchar("name", 50)
    val description = varchar("description", 255)
    val northId = integer("northId")
    val southId = integer("southId")
    val eastId = integer("eastId")
    val westId = integer("westId")
    val upId = integer("upId")
    val downId = integer("downId")
}