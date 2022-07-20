package kotlinmudv2.test

import kotlinmudv2.database.createConnection
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.game.createContainer
import kotlinmudv2.observer.Observer
import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.instance

fun createTestService(): TestService {
    val container = createContainer()
    val eventService by container.instance<EventService>()
    val observers by container.instance<Map<EventType, List<Observer>>>(tag = "observers")
    createConnection()
    eventService.observers = observers
    return TestService(container)
}
