package kotlinmudv2.game

import kotlinmudv2.action.actions.createLookAction
import kotlinmudv2.action.actions.createNorthAction
import kotlinmudv2.event.EventService
import kotlinmudv2.event.EventType
import kotlinmudv2.observer.Observer
import kotlinmudv2.observer.ClientConnectedObserver
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.observer.ReadClientsObserver
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.SocketService
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

fun createContainer(): DI {
    return DI {
        // services
        bindSingleton { EventService() }
        bindSingleton { ClientService() }
        bindSingleton { SocketService(instance(), instance(), 9999) }
        bindSingleton { GameService(instance()) }

        // actions
        bindSingleton {
            listOf(
                createLookAction(),
                createNorthAction(),
            )
        }

        // observers
        bindProvider(tag = "clientConnected") { ClientConnectedObserver() }
        bindProvider(tag = "readClients") { ReadClientsObserver(instance()) }
        bindProvider(tag = "processClientBuffer") { ProcessClientBufferObserver(instance(), instance()) }

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