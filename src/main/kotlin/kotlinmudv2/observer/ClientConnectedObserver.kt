package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.log.logger

class ClientConnectedObserver : Observer {
    private val logger = logger(this)

    override suspend fun <T> invokeAsync(event: Event<T>) {
        logger.info("client connected")
    }
}
