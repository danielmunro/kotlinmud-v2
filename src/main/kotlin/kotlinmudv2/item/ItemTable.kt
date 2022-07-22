package kotlinmudv2.item

import org.jetbrains.exposed.dao.id.IntIdTable

object ItemTable : IntIdTable() {
    val name = varchar("name", 50)
    val description = varchar("description", 255)
    val itemType = varchar("itemType", 64)
}