package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.socket.SocketService

class ReadClientsObserver(private val socketService: SocketService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        socketService.readIntoBuffers()
    }
}