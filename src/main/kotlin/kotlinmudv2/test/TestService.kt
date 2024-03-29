package kotlinmudv2.test

import io.github.serpro69.kfaker.Faker
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.Response
import kotlinmudv2.game.Attribute
import kotlinmudv2.item.Item
import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemFlag
import kotlinmudv2.item.ItemService
import kotlinmudv2.item.ItemType
import kotlinmudv2.mob.Disposition
import kotlinmudv2.mob.FightService
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobEntity
import kotlinmudv2.mob.MobFlag
import kotlinmudv2.mob.MobService
import kotlinmudv2.mob.PlayerMob
import kotlinmudv2.mob.Role
import kotlinmudv2.mob.races.createHumanRace
import kotlinmudv2.observer.ProcessClientBufferObserver
import kotlinmudv2.room.Exit
import kotlinmudv2.room.Room
import kotlinmudv2.room.RoomEntity
import kotlinmudv2.room.RoomService
import kotlinmudv2.room.startRoomId
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
    private val faker = container.direct.instance<Faker>()
    private var potentialTarget: Mob? = null
    var startRoom: Room
    init {
        val roomEntity = transaction {
            RoomEntity.new {
                name = "foo"
                description = "bar"
                exits = mutableListOf()
            }
        }
        startRoom = roomService.getRoom(roomEntity.id.value)!!
    }

    private val client = Client(
        SocketChannel.open(),
        mobService.createPlayerMob(
            "foo",
            "bar",
            createHumanRace(),
            Role.Warrior,
        ),
    ).also {
        transaction {
            it.mob!!.roomId = startRoom.id
        }
    }

    fun getPlayerMob(): PlayerMob {
        return client.mob!!
    }

    fun createRoom(destination: RoomEntity, sourceId: Int, exit: Exit): Room {
        return roomService.connectRooms(sourceId, destination, exit)
    }

    fun createMob(flags: List<MobFlag> = listOf()): Mob {
        return mobService.createMobEntity(
            faker.funnyName.name(),
            "this is a test",
            "a test mob created by TestService",
            createHumanRace(),
            startRoom.id,
            mutableMapOf(
                Pair(Attribute.Hp, 20),
                Pair(Attribute.Mana, 100),
                Pair(Attribute.Moves, 100),
            ),
            mutableListOf(),
            flags,
        ).also {
            potentialTarget = it
        }
    }

    fun createShopkeeper(): Mob {
        return createMob(listOf(MobFlag.Shopkeeper))
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

    fun createPotionForShopkeeper(): Item {
        return itemService.createFromEntity(
            createPotion().also {
                transaction {
                    it.room = RoomEntity.findById(startRoomId)?.id
                }
            }
        ).also {
            potentialTarget?.items?.add(it)
        }
    }

    fun createPotionInRoom(): Item {
        return itemService.createFromEntity(
            createPotion().also {
                transaction {
                    it.room = RoomEntity.findById(startRoomId)?.id
                }
            }
        ).also {
            startRoom.items.add(it)
        }
    }

    fun createPotionInInventory(): Item {
        return itemService.createFromEntity(
            createPotion().also {
                transaction {
                    it.mobInventory = MobEntity.findById(getPlayerMob().id)?.id
                }
            }
        ).also {
            client.mob!!.items.add(it)
        }
    }

    fun createPotionInInventory(amount: Int): List<Item> {
        val entity = createPotion().also {
            transaction {
                it.mobInventory = MobEntity.findById(getPlayerMob().id)?.id
            }
        }
        val items = mutableListOf<Item>()
        for (i in 1..amount) {
            items.add(
                itemService.createFromEntity(entity).also {
                    client.mob!!.items.add(it)
                }
            )
        }
        return items
    }

    fun createDonationPitInRoom(): Item {
        return itemService.createFromEntity(
            transaction {
                ItemEntity.new {
                    name = "a donation pit"
                    brief = "a donation pit is here"
                    description = "a donation pit is here"
                    itemType = ItemType.Furniture.toString()
                    room = RoomEntity.findById(startRoom.id)?.id
                    attributes = mutableMapOf()
                    affects = mutableMapOf()
                    material = "stone"
                    level = 1
                    weight = 1
                    value = 0
                    flags = mutableListOf(
                        ItemFlag.NoGet,
                    )
                }
            }
        ).also {
            startRoom.items.add(it)
        }
    }

    fun createSwordInInventory(modifier: (ItemEntity) -> Unit = { }): Item {
        return createSword(modifier).also {
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

    fun repeatUntilSuccessful(input: String, reset: (Mob) -> Unit): Response? {
        for (i in 1..100) {
            val response = handleRequest(input)
            if (response.actionStatus == ActionStatus.Success) {
                return response
            }
            reset(getPlayerMob())
        }
        return null
    }

    fun repeatUntilFailed(input: String, reset: (Mob) -> Unit): Response? {
        for (i in 1..100) {
            val response = handleRequest(input)
            if (response.actionStatus == ActionStatus.Failure) {
                return response
            }
            reset(getPlayerMob())
        }
        return null
    }

    fun executeFight() {
        container.direct.instance<FightService>().execute()
    }

    private fun getProcessClientBufferObserver(): ProcessClientBufferObserver {
        return container.direct.instance(tag = "processClientBuffer")
    }

    private fun createSword(modifier: (ItemEntity) -> Unit = {}): Item {
        return itemService.createFromEntity(
            transaction {
                ItemEntity.new {
                    name = "a sword"
                    brief = "a sword is lying here"
                    description = "a practice sword is lying here"
                    itemType = ItemType.Equipment.toString()
                    mobInventory = MobEntity.findById(client.mob!!.id)?.id
                    material = "iron"
                    attributes = mutableMapOf()
                    affects = mutableMapOf()
                    level = 1
                    weight = 1
                    value = 0
                    flags = mutableListOf()
                }.also {
                    modifier(it)
                }
            }
        )
    }

    private fun createPotion(): ItemEntity {
        return transaction {
            ItemEntity.new {
                name = "a potion"
                brief = "a strange potion is lying here"
                description = "a strange potion is lying here"
                itemType = ItemType.Potion.toString()
                room = RoomEntity.findById(startRoom.id)?.id
                attributes = mutableMapOf()
                affects = mutableMapOf()
                material = "potion"
                level = 1
                weight = 1
                value = 1
                flags = mutableListOf()
            }
        }
    }
}
