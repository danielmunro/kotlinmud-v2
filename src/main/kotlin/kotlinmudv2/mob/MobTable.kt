package kotlinmudv2.mob

import org.jetbrains.exposed.dao.id.IntIdTable

object MobTable : IntIdTable() {
    val canonicalId = integer("canonicalId")
    val name = varchar("name", 50)
    val brief = varchar("brief", 255)
    val description = text("description")
    val hp = varchar("hp", 64)
    val mana = varchar("mana", 64)
    val moves = varchar("moves", 64)
    val roomId = integer("roomId")
    val race = varchar("race", 50)
    val level = integer("level")
    val attributes = text("attributes")
    val affects = text("affects")
    val flags = text("flags")
    val disposition = varchar("disposition", 50)
    val maxInRoom = integer("maxInRoom")
    val maxInGame = integer("maxInGame")
}
