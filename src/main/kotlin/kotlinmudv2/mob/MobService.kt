package kotlinmudv2.mob

import kotlinmudv2.item.ItemService
import org.jetbrains.exposed.sql.transactions.transaction

class MobService(private val itemService: ItemService) {
    private val mobs = mutableMapOf<Int, MutableList<Mob>>()
    private val mobRooms = mutableMapOf<Int, MutableList<Mob>>()

    fun createMobEntity(name: String, description: String): Mob {
        val entity = transaction {
            MobEntity.new {
                this.name = name
                this.description = description
                brief = ""
                hp = 0
                maxHp = 0
                mana = 0
                maxMana = 0
                moves = 0
                maxMoves = 0
                roomId = 1
            }
        }
        return createMobInstance(entity.id.value)!!
    }

    fun moveMob(mob: Mob, roomId: Int) {
        mobRooms[mob.roomId]?.remove(mob)
        mob.roomId = roomId
        addToMobRooms(mob)
    }

    fun getMobsForRoom(roomId: Int): List<Mob> {
        return mobRooms[roomId] ?: listOf()
    }

    private fun createMobInstance(id: Int): Mob? {
        return transaction { MobEntity.findById(id) }?.let {
            mapMob(it)
        }?.also {
            addToMobs(it)
            addToMobRooms(it)
        }
    }

    private fun addToMobs(mob: Mob) {
        if (mobs[mob.id] == null) {
            mobs[mob.id] = mutableListOf()
        }
        mobs[mob.id]!!.add(mob)
    }

    private fun addToMobRooms(mob: Mob) {
        if (mobRooms[mob.roomId] == null) {
            mobRooms[mob.roomId] = mutableListOf()
        }
        mobRooms[mob.roomId]!!.add(mob)
    }

    private fun mapMob(entity: MobEntity): Mob {
        return Mob(
            entity.id.value,
            entity.name,
            entity.brief,
            entity.description,
            transaction { entity.items.map { itemService.createFromEntity(it) } },
            entity.hp,
            entity.maxHp,
            entity.mana,
            entity.maxMana,
            entity.moves,
            entity.maxMoves,
            entity.roomId,
        )
    }
}