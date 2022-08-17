package kotlinmudv2.action

import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemService
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Room
import kotlinmudv2.room.RoomService
import kotlinmudv2.skill.Skill
import kotlinmudv2.socket.Client
import kotlinmudv2.socket.ClientService

class ActionService(
    private val roomService: RoomService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val clientService: ClientService,
    private val skills: List<Skill>,
    private val spells: List<Skill>,
) {
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
}
