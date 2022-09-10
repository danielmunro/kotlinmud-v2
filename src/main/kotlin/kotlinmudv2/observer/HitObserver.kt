package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.game.AffectType
import kotlinmudv2.mob.DeathService
import kotlinmudv2.mob.Hit
import kotlinmudv2.socket.ClientService

class HitObserver(
    private val clientService: ClientService,
    private val deathService: DeathService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val hit = event.subject as Hit
        val damage = if (hit.defender.affectedBy(AffectType.Sanctuary))
            hit.damage / 2
        else
            hit.damage
        hit.defender.hp -= damage
        hit.roomMessage?.also { clientService.sendToRoom(it) }
        deathService.damageReceived(hit.attacker, hit.defender)
    }
}
