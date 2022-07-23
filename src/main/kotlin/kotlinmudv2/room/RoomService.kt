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

    private fun mapRoom(entity: RoomEntity): Room {
        return Room(
            entity.id.value,
            entity.name,
            entity.description,
            transaction { entity.items.map { itemService.createFromEntity(it) } },
            entity.northId,
            entity.southId,
            entity.eastId,
            entity.westId,
            entity.upId,
            entity.downId,
        )
    }
}