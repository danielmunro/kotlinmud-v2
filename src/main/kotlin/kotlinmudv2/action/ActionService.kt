package kotlinmudv2.action

import kotlinmudv2.event.EventService
import kotlinmudv2.event.createHitEvent
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemService
import kotlinmudv2.mob.Hit
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Room
import kotlinmudv2.room.RoomService
import kotlinmudv2.socket.Client
import kotlinmudv2.socket.ClientService
import kotlinmudv2.socket.RoomMessage

class ActionService(
    private val roomService: RoomService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val clientService: ClientService,
    private val eventService: EventService,
) {
    fun sendToRoom(roomMessage: RoomMessage) {
        clientService.sendToRoom(roomMessage)
    }

    fun getClients(): List<Client> {
        return clientService.getClients()
    }

    fun getRoom(id: Int): Room? {
        return roomService.getRoom(id)
    }

    fun getMobsInRoom(id: Int): List<Mob> {
        return mobService.getMobsForRoom(id)
    }

    fun moveMob(mob: Mob, direction: Direction): Int? {
        return getRoom(mob.roomId)?.let { room ->
            room.exits.find {
                it.direction == direction
            }?.let {
                mobService.moveMob(mob, it.roomId)
                it.roomId
            }
        }
    }

    fun cloneItem(item: Item): Item {
        return itemService.clone(item)
    }

    suspend fun doHit(hit: Hit) {
        eventService.publish(createHitEvent(hit))
    }
}
