package kotlinmudv2.game

import kotlinmudv2.event.EventService
import kotlinmudv2.event.createGameLoopEvent
import kotlinmudv2.event.createPulseEvent
import kotlinmudv2.event.createTickEvent
import kotlinx.coroutines.runBlocking
import java.lang.System.currentTimeMillis

const val tickLengthInMillis = 25000
const val pulseLengthInMillis = 1000

class GameService(private val eventService: EventService) {
    fun start() {
        var lastTick = currentTimeMillis()
        var lastPulse = currentTimeMillis()
        while (true) {
            runBlocking {
                eventService.publish(createGameLoopEvent())
            }
            currentTimeMillis().let {
                if (it - lastPulse > pulseLengthInMillis) {
                    lastPulse = it
                    runBlocking {
                        eventService.publish(createPulseEvent())
                    }
                }
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
