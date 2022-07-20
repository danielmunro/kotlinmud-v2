package kotlinmudv2.action

import kotlinmudv2.mob.Mob
import kotlinmudv2.room.Direction
import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction

class ActionService {
    fun getRoom(id: Int): RoomEntity? {
        return transaction { RoomEntity.findById(id) }
    }

    fun moveMob(mob: Mob, direction: Direction): Int? {
        return getRoom(mob.roomId)?.let { room ->
            when (direction) {
                Direction.North -> room.northId
                Direction.South -> room.southId
                Direction.East -> room.eastId
                Direction.West -> room.westId
                Direction.Up -> room.upId
                Direction.Down -> room.downId
            }?.let {
                mob.roomId = it
                it
            }
        }
    }
}