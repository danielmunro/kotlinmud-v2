package kotlinmudv2.action

import kotlinmudv2.mob.Mob
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Room
import kotlinmudv2.room.RoomService

class ActionService(private val roomService: RoomService) {
    fun getRoom(id: Int): Room? {
        return roomService.getRoom(id)
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