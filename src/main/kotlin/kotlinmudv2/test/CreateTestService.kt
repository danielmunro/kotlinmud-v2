package kotlinmudv2.test

import kotlinmudv2.database.createConnection
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.game.createContainer
import kotlinmudv2.observer.Observer
import org.kodein.di.instance

fun createTestService(): TestService {
    val container = createContainer(0)
    val eventService by container.instance<EventService>()
    val observers by container.instance<Map<EventType, List<Observer>>>(tag = "observers")
    createConnection()
    eventService.observers = observers
    return TestService(container)
}
