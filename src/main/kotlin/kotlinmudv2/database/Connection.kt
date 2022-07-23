package kotlinmudv2.database

import kotlinmudv2.item.ItemTable
import kotlinmudv2.mob.MobTable
import kotlinmudv2.room.RoomTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun createConnection(): Database {
    return Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver").also {
        transaction {
            SchemaUtils.create(
                ItemTable,
                MobTable,
                RoomTable,
            )
        }
    }
}
