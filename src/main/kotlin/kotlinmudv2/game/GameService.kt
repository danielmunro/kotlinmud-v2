package kotlinmudv2.game

import kotlinmudv2.event.EventService
import kotlinmudv2.event.createGameLoopEvent
import kotlinmudv2.event.createTickEvent
import kotlinx.coroutines.runBlocking
import java.lang.System.currentTimeMillis

val tickLengthInMillis = 25000

class GameService(private val eventService: EventService) {
    fun start() {
        var lastTick = currentTimeMillis()
        while (true) {
            runBlocking {
                eventService.publish(createGameLoopEvent())
            }
            currentTimeMillis().let {
                if (it - lastTick > tickLengthInMillis) {
                    lastTick = it
                    runBlocking {
                        eventService.publish(createTickEvent())
                    }
                }
            }
        }
    }
}
