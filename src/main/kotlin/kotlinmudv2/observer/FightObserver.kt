package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.fight.FightService

class FightObserver(private val fightService: FightService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        fightService.execute()
    }
}
