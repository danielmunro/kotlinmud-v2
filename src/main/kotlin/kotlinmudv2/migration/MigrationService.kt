package kotlinmudv2.migration

import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.MobEntity
import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.NumberFormatException

class MigrationService(private val data: String) {
    private var cursor = 0
    private var buffer = ""
    private var lastRoom: RoomEntity? = null
    private val mobModels = mutableMapOf<Int, Map<String, String>>()
    private val mobResets = mutableMapOf<Int, MutableList<MobReset>>()
    private var lastMobReset: MobReset? = null
    private val itemModels = mutableMapOf<Int, Map<String, String>>()
    private val itemRoomResets = mutableMapOf<Int, MutableList<ItemRoomReset>>()
    private val itemMobResets = mutableMapOf<Int, MutableList<ItemMobReset>>()

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
            readIntoBuffer()
            if (buffer.endsWith("\n")) {
                evaluateLine()
            }
        }
        hydrateMobs()
    }

    private fun evaluateLine() {
        val line = buffer
        buffer = ""
        when(line) {
            "#ROOMS\n" -> {
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
            }
            "#MOBILES\n" -> parseMobs()
            "#RESETS\n" -> parseResets()
            "#OBJECTS\n" -> parseItems()
        }
    }

    private fun hydrateMobs() {
        mobModels.forEach { (id, props) ->
            mobResets[id]?.forEach {
                val parts = props["flags3"]!!.split(" ")
                val inventory = itemMobResets[id]?.filter { !it.isEquipped } ?: listOf()
                val equipped = itemMobResets[id]?.filter { it.isEquipped } ?: listOf()
                transaction {
                    MobEntity.new {
                        canonicalId = id
                        name = props["name"]!!.trim()
                        brief = props["brief"]!!.trim()
                        description = props["description"]!!.trim()
                        race = mapRace(props["race"]!!.trim().capitalize())
                        hp = parts[2]
                        mana = parts[3]
                        moves = parts[4]
                        maxInGame = it.maxInGame
                        maxInRoom = it.maxInRoom
                        roomId = it.roomId
                        attributes = mutableMapOf()
                        disposition = Disposition.Standing.toString()
                        affects = mutableMapOf()
                    }.also { mob ->
                        inventory.forEach {
                            itemModels[it.itemId]?.also {
                                ItemEntity.new {
                                    name = it["name"]!!.trim()
                                    brief = it["brief"]!!.trim()
                                    description = it["description"]!!.trim()
                                    mobInventory = mob.id
                                    itemType = ItemType.Indeterminate.toString()
                                    attributes = mutableMapOf()
                                    affects = mutableMapOf()
                                }
                            } ?: println("item model missing ${it.itemId}")

                        }
                        equipped.forEach {
                            itemModels[it.itemId]?.also {
                                ItemEntity.new {
                                    name = it["name"]!!.trim()
                                    brief = it["brief"]!!.trim()
                                    description = it["description"]!!.trim()
                                    mobEquipped = mob.id
                                    itemType = ItemType.Indeterminate.toString()
                                    attributes = mutableMapOf()
                                    affects = mutableMapOf()
                                }
                            } ?: println("item model missing ${it.itemId}")
                        }
                    }
                }
            }
        }
    }

    private fun parseResets() {
        while (true) {
            readUntil("\n")
            val commentPos = buffer.indexOf("*")
            val reset = buffer.take(if (commentPos > -1) commentPos else 0).trim()
            if (buffer == "S\n") {
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
                    if (itemMobResets[mobId] == null) {
                        itemMobResets[mobId] = mutableListOf()
                    }
                    itemMobResets[mobId]!!.add(
                        ItemMobReset(
                            mobId,
                            parts[2].toInt(),
                            parts[1].toInt(),
                            parts[3].toInt(),
                            false,
                        )
                    )
                }
                "E" -> {
                    val mobId = lastMobReset!!.mobId
                    if (itemMobResets[mobId] == null) {
                        itemMobResets[mobId] = mutableListOf()
                    }
                    itemMobResets[mobId]!!.add(
                        ItemMobReset(
                            mobId,
                            parts[2].toInt(),
                            parts[1].toInt(),
                            parts[3].toInt(),
                            true,
                        )
                    )
                }
                "D" -> {}
                "O" -> {}
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
                val itemId = buffer.dropLast(1).toInt()
                if (itemId == 0) {
                    return
                }
                readUntil("~")
                readUntil("~")
                val name = buffer.dropLast(1)
                readUntil("~")
                val description = buffer.dropLast(1)
                readUntil("~")
                val material = buffer
                readUntil("\n")
                val flags1 = buffer
                readUntil("\n")
                val flags2 = buffer
                readUntil("\n")
                val flags3 = buffer
                itemModels[itemId] = mapOf(
                    Pair("name", name),
                    Pair("brief", name),
                    Pair("description", description),
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
                mobModels[mobId] = mapOf(
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
