package kotlinmudv2.action

import kotlinmudv2.item.Item
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Mob
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Room
import kotlinmudv2.socket.Client
import kotlinx.coroutines.runBlocking

class Request(
    val actionService: ActionService,
    val mob: Mob,
    val context: Map<Int, Any>,
    val input: String,
) {
    fun getRoom(id: Int = mob.roomId): Room? {
        return actionService.getRoom(id)
    }

    fun moveMob(direction: Direction): Int? {
        return actionService.moveMob(mob, direction)
    }

    fun getMobsInRoom(): List<Mob> {
        return actionService.getMobsInRoom(mob.roomId)
    }

    fun cloneItem(item: Item): Item {
        return actionService.cloneItem(item)
    }

    fun getClients(): List<Client> {
        return actionService.getClients()
    }

    fun doHit(hit: Hit) {
        runBlocking { actionService.doHit(hit) }
    }

    fun respondToRoom(toActionCreator: String, toRoom: String): Response {
        return Response(
            mob,
            toActionCreator,
            toRoom,
        )
    }

    fun respondToRoomWithTarget(toActionCreator: String, toRoom: String, target: Mob, toTarget: String): Response {
        return Response(
            mob,
            toActionCreator,
            toRoom,
            target,
            toTarget,
        )
    }

    fun respond(message: String): Response {
        return Response(
            mob,
            message,
        )
    }

    fun respondError(message: String): Response {
        return Response(
            mob,
            message,
            null,
            null,
            null,
            ActionStatus.Error,
        )
    }

    fun respondFailure(toActionCreator: String, toRoom: String): Response {
        return Response(
            mob,
            toActionCreator,
            toRoom,
            null,
            null,
            ActionStatus.Failure,
        )
    }

    fun respondFailureWithTarget(toActionCreator: String, toRoom: String, target: Mob, toTarget: String): Response {
        return Response(
            mob,
            toActionCreator,
            toRoom,
            target,
            toTarget,
            ActionStatus.Failure,
        )
    }
}
