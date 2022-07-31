package kotlinmudv2.migration

import kotlinmudv2.mob.MobEntity
import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.NumberFormatException

class MigrationService(private val data: String) {
    private var cursor = 0
    private var buffer = ""
    private var lastRoom: RoomEntity? = null
    private val mobs = mutableMapOf<Int, Map<String, String>>()

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
            try {
                parseRooms()
            } catch (e: NumberFormatException) {
                println("last buffer: '$buffer'")
                println("last room ID: ${lastRoom?.id}")
                throw e
            } catch (e: ExposedSQLException) {
                println("last buffer: '$buffer'")
                println("last room ID: ${lastRoom?.id}")
                throw e
            }
        } else if (line == "#MOBILES\n") {
            parseMobs()
        }
    }

    private fun parseMobs() {
        while (true) {
            val addition = readIntoBuffer()
            if (addition == "#") {
                readUntil("\n")
                val mobId = buffer.dropLast(1).toInt()
                if (mobId == 0) {
                    return
                }
                readUntil("~")
                readUntil("~")
                val name = buffer.dropLast(1)
                readUntil("~")
                val brief = buffer.dropLast(1)
                readUntil("~")
                val description = buffer.dropLast(1)
                readUntil("~")
                val race = buffer.dropLast(1)

                // null
                readUntil("\n")
                readUntil("\n")
                readUntil("\n")

                readUntil("\n")
                val flags1 = buffer

                readUntil("\n")
                val flags2 = buffer

                readUntil("\n")
                val flags3 = buffer

                readUntil("\n")
                val flags4 = buffer

                readUntil("\n")
                val flags5 = buffer

                readUntil("\n")
                val flags6 = buffer
                mobs[mobId] = mapOf(
                    Pair("name", name),
                    Pair("brief", brief),
                    Pair("description", description),
                    Pair("race", race),
                    Pair("flags1", flags1),
                    Pair("flags2", flags2),
                    Pair("flags3", flags3),
                    Pair("flags4", flags4),
                    Pair("flags5", flags5),
                    Pair("flags6", flags6),
                )

//                transaction {
//                    MobEntity.new {
//                        this.name = name
//                        this.brief = brief
//                        this.description = description
//                        this.race = race
//                        hp = 20
//                        mana = 100
//                        moves = 100
//                        maxInGame = 1
//                        maxInRoom = 1
//                    }
//                }
            }
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
                    if (direction.startsWith("M")) {
                        continue
                    }
                    if (direction.startsWith("B")) {
                        continue
                    }
                    if (direction.startsWith("C")) {
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
                lastRoom = transaction {
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
