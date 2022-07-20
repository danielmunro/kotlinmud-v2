package kotlinmudv2.test

import kotlinmudv2.action.Response
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.socket.Client
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import java.nio.channels.SocketChannel


class TestService(private val container: DI) {
    val startRoom = transaction {
        RoomEntity.new {
            name = "foo"
            description = "bar"
        }
    }
    val client = Client(SocketChannel.open())

    fun getProcessClientBufferObserver(): ProcessClientBufferObserver {
        return container.direct.instance(tag = "processClientBuffer")
    }

    fun handleRequest(input: String): Response {
        return getProcessClientBufferObserver().handleRequest(
            client,
            input,
        )
    }
}