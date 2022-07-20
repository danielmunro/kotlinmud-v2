package kotlinmudv2.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmudv2.database.createConnection
import kotlinmudv2.event.Event
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.game.createContainer
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.socket.Client
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.kodein.di.instance
import java.nio.channels.SocketChannel

class ProcessClientBufferObserverTest {
    @Test
    fun testCanLook() {
        // given
        val container = createContainer()
        val eventService by container.instance<EventService>()
        val observers by container.instance<Map<EventType, List<Observer>>>(tag = "observers")
        createConnection()
        val room = transaction {
            RoomEntity.new {
                name = "foo"
                description = "bar"
            }
        }
        eventService.observers = observers
        val processClientBufferObserver by container.instance<ProcessClientBufferObserver>(tag = "processClientBuffer")
        val client = Client(SocketChannel.open())

        // when
        val response = runBlocking {
            processClientBufferObserver.handleRequest(
                client,
                "look",
            )
        }

        // then
        assertThat(response.toActionCreator).isEqualTo("${room.name}\n${room.description}\n")
    }
}