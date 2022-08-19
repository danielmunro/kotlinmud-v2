package kotlinmudv2

import kotlinmudv2.database.createConnection
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.event.createTickEvent
import kotlinmudv2.game.GameService
import kotlinmudv2.game.WebServerService
import kotlinmudv2.game.createContainer
import kotlinmudv2.migration.HydrationService
import kotlinmudv2.migration.ItemMobReset
import kotlinmudv2.migration.ItemRoomReset
import kotlinmudv2.migration.MigrationService
import kotlinmudv2.migration.MobReset
import kotlinmudv2.observer.Observer
import kotlinx.coroutines.runBlocking
import org.kodein.di.direct
import org.kodein.di.instance
import java.io.File

fun main(args: Array<String>) {
    if (args.getOrNull(0) == "migrate") {
        File("data.db").delete()
        createConnection()
        val roomModels = mutableMapOf<Int, Map<String, String?>>()
        val mobModels = mutableMapOf<Int, Map<String, String>>()
        val itemModels = mutableMapOf<Int, Map<String, String>>()
        val mobResets = mutableMapOf<Int, MutableList<MobReset>>()
        val itemRoomResets = mutableMapOf<Int, MutableList<ItemRoomReset>>()
        val itemMobInventoryResets = mutableMapOf<Int, MutableList<ItemMobReset>>()
        val itemMobEquippedResets = mutableMapOf<Int, MutableList<ItemMobReset>>()
        File("sourceData/").listFiles()?.forEach {
            println("reading ${it.name}")
            MigrationService(it.readText()).also { service ->
                service.read()
                roomModels += service.roomModels
                mobModels += service.mobModels
                itemModels += service.itemModels
                mobResets += service.mobResets
                itemRoomResets += service.itemRoomResets
                itemMobInventoryResets += service.itemMobInventoryResets
                itemMobEquippedResets += service.itemMobEquippedResets
            }
        }
        println("hydrating models into entities")
        val container = createContainer(0)
        HydrationService(
            roomModels,
            mobModels,
            itemModels,
            mobResets,
            itemRoomResets,
            itemMobInventoryResets,
            itemMobEquippedResets,
            container.direct.instance(),
        ).hydrate()
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
