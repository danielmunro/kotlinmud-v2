package kotlinmudv2.migration

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Exit
import kotlinmudv2.room.ExitStatus
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.lang.NumberFormatException

class ExitsToken : TypeToken<MutableList<Exit>>()
val gson = Gson()

class MigrationService(private val data: String) {
    private var cursor = 0
    private var buffer = ""
    private var lastMobReset: MobReset? = null
    val roomModels = mutableMapOf<Int, Map<String, String?>>()
    val mobModels = mutableMapOf<Int, MutableMap<String, String>>()
    val itemModels = mutableMapOf<Int, Map<String, String>>()
    val mobResets = mutableMapOf<Int, MutableList<MobReset>>()
    val itemRoomResets = mutableMapOf<Int, MutableList<ItemRoomReset>>()
    val itemMobInventoryResets = mutableMapOf<Int, MutableList<ItemMobReset>>()
    val itemMobEquippedResets = mutableMapOf<Int, MutableList<ItemMobReset>>()

    companion object {
        fun mapRace(race: String): String {
            return when (race) {
                "Water fowl" -> "Bird"
                "Half-elf" -> "Halfling"
                "School monster" -> "Monster"
                "Song bird" -> "Bird"
                "Fido" -> "Dog"
                "Centipede" -> "Insect"
                "Doll" -> "Unique"
                else -> race
            }
        }
    }

    fun read() {
        while (cursor < data.length) {
            readUntil("\n")
            evaluateLine()
        }
    }

    private fun evaluateLine() {
        val line = buffer
        buffer = ""
        when (line) {
            "#ROOMS" -> {
                try {
                    parseRooms()
                } catch (e: NumberFormatException) {
                    println("last buffer: '$buffer'")
                    throw e
                } catch (e: ExposedSQLException) {
                    println("last buffer: '$buffer'")
                    throw e
                }
            }
            "#MOBILES" -> parseMobs()
            "#RESETS" -> parseResets()
            "#OBJECTS" -> parseItems()
            "#SHOPS" -> parseShops()
        }
    }

    private fun parseShops() {
        while (true) {
            readUntil("\n")
            if (buffer == "0") {
                return
            }
            val mobId = buffer.trim().split(" ").first().toInt()
            mobModels[mobId]?.also {
                it["shop"] = "true"
            }
        }
    }

    private fun parseResets() {
        while (true) {
            readUntil("\n")
            val commentPos = buffer.indexOf("*")
            val reset = buffer.take(if (commentPos > -1) commentPos else 0).trim()
            if (buffer == "S") {
                return
            }
            val parts = mutableListOf<String>()
            var isOpen = false
            var value = ""
            reset.forEach {
                val str = it.toString()
                if (str == " ") {
                    if (isOpen) {
                        isOpen = false
                        parts.add(value)
                        value = ""
                    }
                } else {
                    value += str
                    isOpen = true
                }
            }
            if (value != "") {
                parts.add(value)
            }
            if (parts.size == 0) {
                continue
            }
            when (parts[0]) {
                "M" -> {
                    val id = parts[2].toInt()
                    if (mobResets[id] == null) {
                        mobResets[id] = mutableListOf()
                    }
                    mobResets[id]?.add(
                        MobReset(
                            id,
                            parts[4].toInt(),
                            parts[5].toInt(),
                            parts[3].toInt(),
                        ).also {
                            lastMobReset = it
                        }
                    )
                }
                "G" -> {
                    val mobId = lastMobReset!!.mobId
                    if (itemMobInventoryResets[mobId] == null) {
                        itemMobInventoryResets[mobId] = mutableListOf()
                    }
                    itemMobInventoryResets[mobId]!!.add(
                        ItemMobReset(
                            mobId,
                            parts[2].toInt(),
                            parts[1].toInt(),
                            parts[3].toInt(),
                        )
                    )
                }
                "E" -> {
                    val mobId = lastMobReset!!.mobId
                    if (itemMobEquippedResets[mobId] == null) {
                        itemMobEquippedResets[mobId] = mutableListOf()
                    }
                    itemMobEquippedResets[mobId]!!.add(
                        ItemMobReset(
                            mobId,
                            parts[2].toInt(),
                            parts[1].toInt(),
                            parts[3].toInt(),
                        )
                    )
                }
                "D" -> {}
                "O" -> {
                    val itemId = parts[2].toInt()
                    val roomId = parts[4].toInt()
                    if (itemRoomResets[roomId] == null) {
                        itemRoomResets[roomId] = mutableListOf()
                    }
                    itemRoomResets[roomId]!!.add(
                        ItemRoomReset(
                            itemId,
                            roomId,
                            1,
                            1,
                        )
                    )
                }
                "P" -> {}
                "R" -> {}
                else -> {
                    println("NOT FOUND: " + parts[0])
                }
            }
        }
    }

    private fun parseItems() {
        while (true) {
            val addition = readIntoBuffer()
            if (addition == "#") {
                readUntil("\n")
                val itemId = buffer.toInt()
                if (itemId == 0) {
                    return
                }
                readUntil("~")
                readUntil("~")
                val name = buffer
                readUntil("~")
                val brief = buffer
                readUntil("~")
                val material = buffer
                readUntil("\n")
                readUntil("\n")
                val flags1 = buffer
                readUntil("\n")
                val flags2 = buffer
                readUntil("\n")
                val flags3 = buffer
                itemModels[itemId] = mapOf(
                    Pair("name", name),
                    Pair("brief", brief),
                    Pair("description", brief),
                    Pair("material", material),
                    Pair("flags1", flags1),
                    Pair("flags2", flags2),
                    Pair("flags3", flags3),
                )
            }
        }
    }

    private fun parseMobs() {
        while (true) {
            val addition = readIntoBuffer()
            if (addition == "#") {
                readUntil("\n")
                val mobId = buffer.toInt()
                if (mobId == 0) {
                    return
                }
                readUntil("~")
                readUntil("~")
                val name = buffer
                readUntil("~")
                val brief = buffer
                readUntil("~")
                val description = buffer
                readUntil("~")
                val race = buffer

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
                mobModels[mobId] = mutableMapOf(
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
            }
        }
    }

    private fun parseRooms() {
        while (true) {
            val addition = readIntoBuffer()
            if (addition == "#") {
                readUntil("\n")
                val roomId = buffer.toInt()
                if (roomId == 0) {
                    return
                }
                readUntil("~")
                val name = buffer
                readUntil("~")
                val description = buffer
                readUntil("\n")
                readUntil("\n")
                var northId: String? = null
                var southId: String? = null
                var eastId: String? = null
                var westId: String? = null
                var upId: String? = null
                var downId: String? = null
                var north: Exit? = null
                var south: Exit? = null
                var east: Exit? = null
                var west: Exit? = null
                var up: Exit? = null
                var down: Exit? = null
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
                    readUntil("~")
                    cursor += 1
                    val keyword = buffer.trim().let {
                        if (it == "") {
                            null
                        } else {
                            it
                        }
                    }
                    readUntil("\n")
                    val (lockId, keyId, exit) = buffer.split(" ")
                    val getExitStatus = {
                        if (keyword == null) {
                            null
                        } else if (lockId.toInt() <= 0) {
                            ExitStatus.Closed
                        } else {
                            ExitStatus.Locked
                        }
                    }
                    when (direction) {
                        "D0" -> {
                            northId = exit
                            north = Exit(
                                Direction.North,
                                exit.toInt(),
                                keyword,
                                getExitStatus(),
                                lockId.toInt(),
                                keyId.toInt(),
                            )
                        }
                        "D1" -> {
                            eastId = exit
                            east = Exit(
                                Direction.East,
                                exit.toInt(),
                                keyword,
                                getExitStatus(),
                                lockId.toInt(),
                                keyId.toInt(),
                            )
                        }
                        "D2" -> {
                            southId = exit
                            south = Exit(
                                Direction.South,
                                exit.toInt(),
                                keyword,
                                getExitStatus(),
                                lockId.toInt(),
                                keyId.toInt(),
                            )
                        }
                        "D3" -> {
                            westId = exit
                            west = Exit(
                                Direction.West,
                                exit.toInt(),
                                keyword,
                                getExitStatus(),
                                lockId.toInt(),
                                keyId.toInt(),
                            )
                        }
                        "D4" -> {
                            upId = exit
                            up = Exit(
                                Direction.Up,
                                exit.toInt(),
                                keyword,
                                getExitStatus(),
                                lockId.toInt(),
                                keyId.toInt(),
                            )
                        }
                        "D5" -> {
                            downId = exit
                            down = Exit(
                                Direction.Down,
                                exit.toInt(),
                                keyword,
                                getExitStatus(),
                                lockId.toInt(),
                                keyId.toInt(),
                            )
                        }
                    }
                    roomModels[roomId] = mapOf(
                        Pair("name", name),
                        Pair("description", description),
                        Pair("northId", northId),
                        Pair("southId", southId),
                        Pair("eastId", eastId),
                        Pair("westId", westId),
                        Pair("upId", upId),
                        Pair("downId", downId),
                        Pair(
                            "exits",
                            gson.toJson(
                                mutableListOf(
                                    north,
                                    south,
                                    east,
                                    west,
                                    up,
                                    down,
                                ).filterNotNull()
                            )
                        )
                    )
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
        buffer = buffer.dropLast(terminator.length)
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
