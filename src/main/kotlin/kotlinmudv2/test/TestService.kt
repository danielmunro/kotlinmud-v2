package kotlinmudv2.test

import kotlinmudv2.action.Response
import kotlinmudv2.fight.FightService
import kotlinmudv2.game.Attribute
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemService
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobEntity
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.Race
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.room.Direction
import kotlinmudv2.room.Room
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.room.RoomService
import kotlinmudv2.socket.Client
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import java.nio.channels.SocketChannel

class TestService(private val container: DI) {
    private val roomService = container.direct.instance<RoomService>()
    private val mobService = container.direct.instance<MobService>()
    private val itemService = container.direct.instance<ItemService>()
    var startRoom: Room
    var potentialTarget: Mob? = null
    init {
        val roomEntity = transaction {
            RoomEntity.new {
                name = "foo"
                description = "bar"
            }
        }
        startRoom = roomService.getRoom(roomEntity.id.value)!!
    }

    private val client = Client(
        SocketChannel.open(),
        mobService.createPlayerMob("foo", "bar", Race.Human),
    ).also {
        transaction {
            it.mob!!.roomId = startRoom.id
        }
    }

    fun getPlayerMob(): Mob {
        return client.mob!!
    }

    fun createRoom(destination: RoomEntity, sourceId: Int, direction: Direction): Room {
        return roomService.connectRooms(sourceId, destination, direction)
    }

    fun createMob(): Mob {
        return mobService.createMobEntity(
            "a test mob",
            "this is a test",
            "a test mob created by TestService",
            Race.Human,
            startRoom.id,
            mutableMapOf(
                Pair(Attribute.Hp, 20),
                Pair(Attribute.Mana, 100),
                Pair(Attribute.Moves, 100),
            ),
            mutableMapOf(),
        ).also {
            potentialTarget = it
        }
    }

    fun moveMob(mob: Mob, roomId: Int) {
        mobService.moveMob(mob, roomId)
    }

    fun setupFight() {
        client.mob?.let {
            it.target = potentialTarget
            it.disposition = Disposition.Fighting
        }
        potentialTarget?.let {
            it.target = client.mob
            it.disposition = Disposition.Fighting
        }
    }

    fun createPotionInRoom(): Item {
        return itemService.createFromEntity(
            transaction {
                ItemEntity.new {
                    name = "a potion"
                    brief = "a strange potion is lying here"
                    description = "a strange potion is lying here"
                    itemType = ItemType.Potion.toString()
                    room = RoomEntity.findById(startRoom.id)?.id
                    attributes = mutableMapOf()
                    affects = mutableMapOf()
                    level = 1
                    weight = 1
                    value = 0
                }
            }
        ).also {
            startRoom.items.add(it)
        }
    }

    fun createPotionInInventory(): Item {
        return itemService.createFromEntity(
            transaction {
                ItemEntity.new {
                    name = "a potion"
                    brief = "a strange potion is lying here"
                    description = "a strange potion is lying here"
                    itemType = ItemType.Potion.toString()
                    mobInventory = MobEntity.findById(client.mob!!.id)?.id
                    attributes = mutableMapOf()
                    affects = mutableMapOf()
                    level = 1
                    weight = 1
                    value = 0
                }
            }
        ).also {
            client.mob!!.items.add(it)
        }
    }

    fun createSwordInInventory(): Item {
        return createSword().also {
            client.mob!!.items.add(it)
        }
    }

    fun createEquippedSword(): Item {
        return createSword().also {
            client.mob!!.equipped.add(it)
        }
    }

    fun handleRequest(input: String): Response {
        return getProcessClientBufferObserver().handleRequest(
            client,
            input,
        )
    }

    fun executeFight() {
        container.direct.instance<FightService>().execute()
    }

    private fun getProcessClientBufferObserver(): ProcessClientBufferObserver {
        return container.direct.instance(tag = "processClientBuffer")
    }

    private fun createSword(): Item {
        return itemService.createFromEntity(
            transaction {
                ItemEntity.new {
                    name = "a sword"
                    brief = "a sword is lying here"
                    description = "a practice sword is lying here"
                    itemType = ItemType.Equipment.toString()
                    mobInventory = MobEntity.findById(client.mob!!.id)?.id
                    attributes = mutableMapOf()
                    affects = mutableMapOf()
                    level = 1
                    weight = 1
                    value = 0
                }
            }
        )
    }
}
