package kotlinmudv2.room

import kotlinmudv2.item.ItemService
import org.jetbrains.exposed.sql.transactions.transaction

class RoomService(private val itemService: ItemService) {
    private val rooms = mutableMapOf<Int, Room>()

    fun getRoom(id: Int): Room? {
        if (rooms[id] == null) {
            transaction { RoomEntity.findById(id) }?.let {
                rooms[id] = mapRoom(it)
            }
        }
        return rooms[id]
    }

    fun createRoom(toEntity: RoomEntity, fromRoomId: Int, direction: Direction): Room {
        val fromEntity = transaction { RoomEntity.findById(fromRoomId)!! }
        transaction {
            when (direction) {
                Direction.North -> {
                    fromEntity.northId = toEntity.id.value
                    toEntity.southId = fromEntity.id.value
                }
                Direction.South -> {
                    fromEntity.southId = toEntity.id.value
                    toEntity.northId = fromEntity.id.value
                }
                Direction.East -> {
                    fromEntity.eastId = toEntity.id.value
                    toEntity.westId = fromEntity.id.value
                }
                Direction.West -> {
                    fromEntity.westId = toEntity.id.value
                    toEntity.eastId = fromEntity.id.value
                }
                Direction.Up -> {
                    fromEntity.upId = toEntity.id.value
                    toEntity.downId = fromEntity.id.value
                }
                Direction.Down -> {
                    fromEntity.downId = toEntity.id.value
                    toEntity.upId = fromEntity.id.value
                }
            }
        }
        rooms.remove(fromRoomId)
        return getRoom(toEntity.id.value)!!
    }

    private fun mapRoom(entity: RoomEntity): Room {
        return Room(
            entity.id.value,
            entity.name,
            entity.description,
            transaction { entity.items.map { itemService.createFromEntity(it) }.toMutableList() },
            entity.northId,
            entity.southId,
            entity.eastId,
            entity.westId,
            entity.upId,
            entity.downId,
        )
    }
}
