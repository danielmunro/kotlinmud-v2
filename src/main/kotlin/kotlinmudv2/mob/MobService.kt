package kotlinmudv2.mob

import com.google.gson.Gson
import kotlinmudv2.crypto.generateSalt
import kotlinmudv2.crypto.hash
import kotlinmudv2.item.ItemService
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class MobService(private val itemService: ItemService) {
    private val mobs = mutableMapOf<Int, MutableList<Mob>>()
    private val mobRooms = mutableMapOf<Int, MutableList<Mob>>()

    fun isPlayerMob(name: String): Boolean {
        return File("./players/$name.json").isFile
    }

    fun hydratePlayerMob(name: String): PlayerMob {
        val data = File("./players/$name.json")
        return Gson().fromJson(data.readText(), PlayerMob::class.java)
    }

    fun createPlayerMob(name: String, password: String, race: Race): PlayerMob {
        val salt = generateSalt()

        return PlayerMob(
            hash(password, salt),
            salt,
            0,
            name,
            "$name is here",
            "",
            race,
            mutableListOf(),
            20,
            100,
            100,
            1,
        )
    }

    fun createMobEntity(name: String, description: String, brief: String, race: Race, roomId: Int): Mob {
        val entity = transaction {
            MobEntity.new {
                this.name = name
                this.description = description
                this.brief = brief
                this.race = race.toString()
                hp = 0
                maxHp = 0
                mana = 0
                maxMana = 0
                moves = 0
                maxMoves = 0
                this.roomId = roomId
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

    fun mapMob(entity: MobEntity): Mob {
        return Mob(
            entity.id.value,
            entity.name,
            entity.brief,
            entity.description,
            Race.valueOf(entity.race),
            transaction { entity.items.map { itemService.createFromEntity(it) } }.toMutableList(),
            entity.hp,
            entity.mana,
            entity.moves,
            entity.roomId,
        )
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
}
