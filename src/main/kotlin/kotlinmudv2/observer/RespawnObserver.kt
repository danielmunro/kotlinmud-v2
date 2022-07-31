package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.mob.MobService

class RespawnObserver(
    private val mobService: MobService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.respawnMobs()
    }
}
