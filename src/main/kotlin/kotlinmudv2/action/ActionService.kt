package kotlinmudv2.action

import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction

class ActionService {
    fun getRoom(id: Int): RoomEntity? {
        return transaction { RoomEntity.findById(id) }
    }
}