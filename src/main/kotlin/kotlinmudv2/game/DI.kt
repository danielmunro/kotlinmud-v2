package kotlinmudv2.game

import kotlinmudv2.action.ActionService
import kotlinmudv2.action.ContextService
import kotlinmudv2.action.actions.createDownAction
import kotlinmudv2.action.actions.createEastAction
import kotlinmudv2.action.actions.createLookAction
import kotlinmudv2.action.actions.createLookAtMobInRoomAction
import kotlinmudv2.action.actions.createNorthAction
import kotlinmudv2.action.actions.createSouthAction
import kotlinmudv2.action.actions.createUpAction
import kotlinmudv2.action.actions.createWestAction
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.item.ItemService
import kotlinmudv2.mob.MobService
import kotlinmudv2.observer.ClientConnectedObserver
import kotlinmudv2.observer.Observer
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.observer.ReadClientsObserver
import kotlinmudv2.room.RoomService
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.SocketService
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

fun createContainer(port: Int): DI {
    return DI {
        // services
        bindSingleton { EventService() }
        bindSingleton { ClientService() }
        bindSingleton { ItemService() }
        bindSingleton { RoomService(instance()) }
        bindSingleton { MobService(instance()) }
        bindSingleton { ContextService(instance(), instance()) }
        bindSingleton { ActionService(instance(), instance()) }
        bindSingleton { SocketService(instance(), instance(), instance(), port) }
        bindSingleton { GameService(instance()) }

        // actions
        bindSingleton {
            listOf(
                createNorthAction(),
                createSouthAction(),
                createEastAction(),
                createWestAction(),
                createUpAction(),
                createDownAction(),
                createLookAtMobInRoomAction(),
                createLookAction(),
            )
        }

        // observers
        bindProvider(tag = "clientConnected") { ClientConnectedObserver() }
        bindProvider(tag = "readClients") { ReadClientsObserver(instance()) }
        bindProvider(tag = "processClientBuffer") { ProcessClientBufferObserver(instance(), instance(), instance()) }

        // list of observers
        bindSingleton<Map<EventType, List<Observer>>>(tag = "observers") {
            mapOf(
                Pair(
                    EventType.ClientConnected,
                    listOf(
                        instance(tag = "clientConnected"),
                    ),
                ),
                Pair(
                    EventType.GameLoop,
                    listOf(
                        instance(tag = "readClients"),
                        instance(tag = "processClientBuffer")
                    ),
                ),
            )
        }
    }
}
