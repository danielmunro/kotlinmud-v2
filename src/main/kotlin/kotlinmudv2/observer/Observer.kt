package kotlinmudv2.observer

import kotlinmudv2.event.Event

interface Observer {
    suspend fun <T> invokeAsync(event: Event<T>)
}
