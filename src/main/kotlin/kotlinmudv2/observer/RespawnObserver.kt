package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.mob.MobService
import kotlin.system.measureTimeMillis

class RespawnObserver(
    private val mobService: MobService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        println("respawn initialized")
        val amount = measureTimeMillis { mobService.respawnMobs() }
        println("respawn complete, elapsed time: $amount ms")
    }
}
