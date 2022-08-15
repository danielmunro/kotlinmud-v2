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

    fun connectRooms(fromRoomId: Int, toEntity: RoomEntity, exit: Exit): Room {
        val fromEntity = transaction { RoomEntity.findById(fromRoomId)!! }
        val fromExits = fromEntity.exits.toMutableList()
        fromExits.add(exit)
        val toExits = toEntity.exits.toMutableList()
        toExits.add(
            Exit(
                opposite(exit.direction),
                fromRoomId,
                exit.keyword,
                exit.status,
                exit.lockId,
                exit.keyId,
            )
        )
        transaction {
            fromEntity.exits = fromExits
            toEntity.exits = toExits
        }
        rooms.remove(fromRoomId)
        return getRoom(toEntity.id.value)!!
    }

    fun mapRoom(entity: RoomEntity): Room {
        return Room(
            entity.id.value,
            entity.name,
            entity.description,
            transaction { entity.items.map { itemService.createFromEntity(it) }.toMutableList() },
            entity.exits,
        )
    }
}
