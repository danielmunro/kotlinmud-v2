package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.mob.MobService

class RegenObserver(
    private val mobService: MobService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.regen()
    }
}
