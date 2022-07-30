package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.item.ItemService
import kotlinmudv2.mob.MobService

class AffectDecayObserver(
    private val mobService: MobService,
    private val itemService: ItemService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.affectDecay()
        itemService.affectDecay()
    }
}
