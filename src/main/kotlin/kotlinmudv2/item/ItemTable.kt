package kotlinmudv2.item

import kotlinmudv2.mob.MobTable
import kotlinmudv2.room.RoomTable
import org.jetbrains.exposed.dao.id.IntIdTable

object ItemTable : IntIdTable() {
    val name = varchar("name", 255)
    val brief = varchar("brief", 255)
    val description = varchar("description", 255)
    val itemType = varchar("itemType", 64)
    val attributes = text("attributes")
    val affects = text("affects")
    val position = varchar("position", 64).nullable()
    val level = integer("level")
    val value = integer("value")
    val weight = integer("weight")
    val material = varchar("material", 64)
    val flags = text("flags")
    val damageRolls = integer("damageRolls").nullable()
    val damageDice = integer("damageDice").nullable()
    val attackVerb = varchar("attackVerb", 64).nullable()
    val damageType = varchar("damageType", 64).nullable()
    val weaponType = varchar("weaponType", 64).nullable()
    val mobInventory = reference("mobInventoryId", MobTable).nullable()
    val mobEquipped = reference("mobEquippedId", MobTable).nullable()
    val room = reference("roomId", RoomTable).nullable()
}
