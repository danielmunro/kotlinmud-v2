package kotlinmudv2.event.observer

import kotlinmudv2.event.Event

interface Observer {
    suspend fun <T> invokeAsync(event: Event<T>)
}
