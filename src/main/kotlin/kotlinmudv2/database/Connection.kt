package kotlinmudv2.database

import kotlinmudv2.item.ItemTable
import kotlinmudv2.mob.MobTable
import kotlinmudv2.room.RoomTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun createTestConnection(): Database {
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

fun createConnection(): Database {
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    return Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC").also {
        transaction {
            SchemaUtils.create(
                ItemTable,
                MobTable,
                RoomTable,
            )
        }
    }
}
