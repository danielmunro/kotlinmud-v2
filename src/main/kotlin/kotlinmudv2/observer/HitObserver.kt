package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.mob.DeathService
import kotlinmudv2.mob.Hit
import kotlinmudv2.socket.ClientService

class HitObserver(
    private val clientService: ClientService,
    private val deathService: DeathService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val hit = event.subject as Hit
        hit.defender.hp -= hit.damage
        clientService.sendToRoom(hit.roomMessage)
        deathService.damageReceived(hit.attacker, hit.defender)
    }
}
