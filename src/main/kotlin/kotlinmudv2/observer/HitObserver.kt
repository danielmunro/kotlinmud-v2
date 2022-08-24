package kotlinmudv2.observer

import kotlinmudv2.event.Event
import kotlinmudv2.mob.DeathService
import kotlinmudv2.mob.Hit
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage

class HitObserver(
    private val clientService: ClientService,
    private val deathService: DeathService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val hit = event.subject as Hit
        hit.defender.hp -= hit.damage
        clientService.sendToRoom(
            RoomMessage(
                hit.attacker,
                "You hit ${hit.defender.name}, causing scratches",
                "${hit.attacker.name} hit ${hit.defender.name}, causing scratches",
                hit.defender,
                "${hit.attacker.name} hit you, causing scratches",
            )
        )

        deathService.damageReceived(hit.attacker, hit.defender)
    }
}
