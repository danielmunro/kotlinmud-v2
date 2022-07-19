package kotlinmudv2.game

import kotlinmudv2.event.EventService
import kotlinmudv2.event.createGameLoopEvent
import kotlinx.coroutines.runBlocking

class GameService(
    private val eventService: EventService,
) {
    private var isRunning = true

    fun start() {
        while (isRunning) {
            runBlocking {
                eventService.publish(createGameLoopEvent())
            }
        }
    }
}