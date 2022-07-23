package kotlinmudv2.observer

import kotlinmudv2.event.Event

class ClientConnectedObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        println("client connected")
    }
}
