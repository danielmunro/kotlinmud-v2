package kotlinmudv2.room

import org.jetbrains.exposed.sql.transactions.transaction

class RoomService {
    private val rooms = mutableMapOf<Int, Room>()

    fun getRoom(id: Int): Room? {
        if (rooms[id] == null) {
            transaction { RoomEntity.findById(id) }?.let {
                rooms[id] = Room(it)
            }
        }
        return rooms[id]
    }
}