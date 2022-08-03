package kotlinmudv2

import kotlinmudv2.database.createConnection
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.event.createTickEvent
import kotlinmudv2.game.GameService
import kotlinmudv2.game.WebServerService
import kotlinmudv2.game.createContainer
import kotlinmudv2.migration.MigrationService
import kotlinmudv2.observer.Observer
import kotlinx.coroutines.runBlocking
import org.kodein.di.instance
import java.io.File

fun main(args: Array<String>) {
    if (args.getOrNull(0) == "migrate") {
        File("data.db").delete()
        createConnection()
        File("sourceData/").listFiles()?.forEach {
            println("reading ${it.name}")
            MigrationService(it.readText()).read()
        }
        System.exit(0)
    }

    createConnection()
    val container = createContainer(9999)
    val gameService by container.instance<GameService>()
    val eventService by container.instance<EventService>()
    val webServer by container.instance<WebServerService>()
    val observers by container.instance<Map<EventType, List<Observer>>>(tag = "observers")

    eventService.observers = observers

    runBlocking {
        eventService.publish(createTickEvent())
    }

    webServer.start()
    gameService.start()
}
