package kotlinmudv2.event.observer

import kotlinmudv2.event.Event

class ClientConnectedObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        println("client connected")
    }
}