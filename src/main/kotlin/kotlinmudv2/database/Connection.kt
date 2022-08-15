package kotlinmudv2.database

import kotlinmudv2.item.ItemTable
import kotlinmudv2.mob.MobTable
import kotlinmudv2.room.RoomTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun createTestConnection(): Database {
    val connection = "regular${getRandomString(8)}"
    return Database.connect("jdbc:h2:mem:$connection;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver").also {
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
