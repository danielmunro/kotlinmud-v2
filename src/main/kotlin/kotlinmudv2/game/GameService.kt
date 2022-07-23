package kotlinmudv2.game

import kotlinmudv2.event.EventService
import kotlinmudv2.event.createGameLoopEvent
import kotlinx.coroutines.runBlocking

class GameService(private val eventService: EventService) {
    fun start() {
        while (true) {
            runBlocking {
                eventService.publish(createGameLoopEvent())
            }
        }
    }
}
