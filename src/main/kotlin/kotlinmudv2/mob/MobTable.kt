package kotlinmudv2.mob

import org.jetbrains.exposed.dao.id.IntIdTable

object MobTable : IntIdTable() {
    val name = varchar("name", 50)
    val brief = varchar("brief", 50)
    val description = varchar("description", 255)
    val hp = integer("hp")
    val mana = integer("mana")
    val moves = integer("moves")
    val roomId = integer("roomId")
    val race = varchar("race", 50)
    val attributes = text("attributes")
    val affects = text("affects")
    val disposition = varchar("disposition", 50)
    val maxInRoom = integer("maxInRoom")
    val maxInGame = integer("maxInGame")
}
