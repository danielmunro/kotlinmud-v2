package kotlinmudv2.mob

import kotlinmudv2.item.ItemService
import org.jetbrains.exposed.sql.transactions.transaction

class MobService(private val itemService: ItemService) {
    private val mobs = mutableMapOf<Int, Mob>()

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

    fun getMob(id: Int): Mob? {
        if (mobs[id] == null) {
            createMobInstance(id)?.let { mobs[id] = it }
        }
        return mobs[id]
    }

    private fun createMobInstance(id: Int): Mob? {
        return transaction { MobEntity.findById(id) }?.let {
            mapMob(it)
        }
    }

    private fun mapMob(entity: MobEntity): Mob {
        return Mob(
            entity.id.value,
            entity.name,
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