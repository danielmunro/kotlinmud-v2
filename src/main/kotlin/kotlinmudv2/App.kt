package kotlinmudv2

import kotlinmudv2.database.createTestConnection
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.game.GameService
import kotlinmudv2.game.WebServerService
import kotlinmudv2.game.createContainer
import kotlinmudv2.observer.Observer
import kotlinmudv2.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.instance

fun main() {
    val container = createContainer(9999)
    val gameService by container.instance<GameService>()
    val eventService by container.instance<EventService>()
    val webServer by container.instance<WebServerService>()
    val observers by container.instance<Map<EventType, List<Observer>>>(tag = "observers")
    createTestConnection()
    eventService.observers = observers

    transaction {
        RoomEntity.new {
            name = "a fountain at the center of town"
            description = "bar"
            northId = 2
        }
        RoomEntity.new {
            name = "market avenue"
            description = "bar"
            southId = 1
        }
    }

    webServer.start()
    gameService.start()
}
