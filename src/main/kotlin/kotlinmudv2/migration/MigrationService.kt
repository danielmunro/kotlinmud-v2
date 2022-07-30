package kotlinmudv2.migration

import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction

class MigrationService(private val data: String) {
    private var cursor = 0
    private var buffer = ""

    fun read() {
        while (cursor < data.length) {
            readIntoBuffer()
            if (buffer.endsWith("\n")) {
                evaluateLine()
            }
        }
    }

    private fun evaluateLine() {
        val line = buffer
        buffer = ""
        if (line == "#ROOMS\n") {
            parseRooms()
        }
    }

    private fun parseRooms() {
        while (true) {
            val addition = readIntoBuffer()
            if (addition == "#") {
                readUntil("\n")
                val roomId = buffer.dropLast(1).toInt()
                if (roomId == 0) {
                    return
                }
                readUntil("~")
                val name = buffer.dropLast(1)
                readUntil("~")
                val description = buffer.dropLast(1)
                readUntil("\n")
                readUntil("\n")
                var northId: Int? = null
                var southId: Int? = null
                var eastId: Int? = null
                var westId: Int? = null
                var upId: Int? = null
                var downId: Int? = null
                while (peek(1).trim() != "S") {
                    readUntil("\n")
                    val direction = buffer.trim()
                    if (direction == "E") {
                        readUntil("~")
                        readUntil("~")
                        readUntil("\n")
                        continue
                    }
                    if (direction.startsWith("H")) {
                        continue
                    }
                    if (direction.startsWith("O")) {
                        continue
                    }
                    readUntil("\n")
                    readUntil("\n")
                    readUntil("\n")
                    val exit = buffer.split(" ").takeLast(1).first().trim().toInt()
                    when (direction) {
                        "D0" -> northId = exit
                        "D1" -> eastId = exit
                        "D2" -> southId = exit
                        "D3" -> westId = exit
                        "D4" -> upId = exit
                        "D5" -> downId = exit
                    }
                }
                transaction {
                    RoomEntity.new(roomId) {
                        this.name = name
                        this.description = description
                        this.northId = northId
                        this.southId = southId
                        this.eastId = eastId
                        this.westId = westId
                        this.upId = upId
                        this.downId = downId
                    }
                }
            }
        }
    }

    private fun readIntoBuffer(): String {
        val addition = data.substring(cursor, cursor + 1)
        buffer += addition
        cursor += 1
        return addition
    }

    private fun readUntil(terminator: String) {
        buffer = ""
        var add = ""
        while (add != terminator) {
            add = readIntoBuffer()
        }
    }

    private fun peek(amount: Int): String {
        val initialCursor = cursor
        val initialBuffer = buffer
        buffer = ""
        for (i in 0..amount) {
            readIntoBuffer()
        }
        val modifiedBuffer = buffer
        buffer = initialBuffer
        cursor = initialCursor
        return modifiedBuffer
    }
}
