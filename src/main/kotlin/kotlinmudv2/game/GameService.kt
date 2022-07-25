package kotlinmudv2.game

import kotlinmudv2.event.EventService
import kotlinmudv2.event.createGameLoopEvent
import kotlinmudv2.event.createTickEvent
import kotlinx.coroutines.runBlocking
import java.lang.System.currentTimeMillis

class GameService(private val eventService: EventService) {
    fun start() {
        var lastTick = currentTimeMillis()
        while (true) {
            runBlocking {
                eventService.publish(createGameLoopEvent())
            }
            currentTimeMillis().let {
                if (it - lastTick > 25000) {
                    lastTick = it
                    runBlocking {
                        eventService.publish(createTickEvent())
                    }
                }
            }
        }
    }
}
