package kotlinmudv2.mob

import org.jetbrains.exposed.dao.id.IntIdTable

object MobTable : IntIdTable() {
    val name = varchar("name", 50)
    val brief = varchar("brief", 50)
    val description = varchar("description", 255)
    val roomId = integer("roomId")
}