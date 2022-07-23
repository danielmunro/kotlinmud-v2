package kotlinmudv2.test

import kotlinmudv2.action.Response
import kotlinmudv2.mob.MobService
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.room.Room
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.room.RoomService
import kotlinmudv2.socket.Client
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import java.nio.channels.SocketChannel


class TestService(private val container: DI) {
    private val roomService = container.direct.instance<RoomService>()
    private val mobService = container.direct.instance<MobService>()
    var startRoom: Room
    init {
        val roomEntity = transaction {
            RoomEntity.new {
                name = "foo"
                description = "bar"
            }
        }
        startRoom = roomService.getRoom(roomEntity.id.value)!!
    }

    private val client = Client(
        SocketChannel.open(),
        mobService.createMobEntity("foo", "bar"),
    ).also {
        transaction {
            it.mob.roomId = startRoom.id
        }
    }

    fun handleRequest(input: String): Response {
        return getProcessClientBufferObserver().handleRequest(
            client,
            input,
        )
    }

    private fun getProcessClientBufferObserver(): ProcessClientBufferObserver {
        return container.direct.instance(tag = "processClientBuffer")
    }
}
