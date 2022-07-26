package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.socket.ClientService

class PersistPlayersObserver(private val clientService: ClientService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        clientService.persistPlayerMobs()
    }
}
