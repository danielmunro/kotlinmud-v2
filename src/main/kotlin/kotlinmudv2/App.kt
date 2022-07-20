package kotlinmudv2

import kotlinmudv2.database.createConnection
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.observer.Observer
import kotlinmudv2.game.GameService
import kotlinmudv2.game.createContainer
import org.kodein.di.instance

fun main() {
    val container = createContainer()
    val gameService by container.instance<GameService>()
    val eventService by container.instance<EventService>()
    val observers by container.instance<Map<EventType, List<Observer>>>(tag = "observers")
    createConnection()
    eventService.observers = observers
    gameService.start()
}