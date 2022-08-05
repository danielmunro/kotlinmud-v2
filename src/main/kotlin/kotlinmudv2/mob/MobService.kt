package kotlinmudv2.mob

import com.google.gson.Gson
import kotlinmudv2.crypto.generateSalt
import kotlinmudv2.crypto.hash
import kotlinmudv2.dice.diceFromString
import kotlinmudv2.game.Affect
import kotlinmudv2.game.Attribute
import kotlinmudv2.item.ItemService
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class MobService(private val itemService: ItemService) {
    private val mobsById = mutableMapOf<Int, MutableList<Mob>>()
    private val mobs = mutableListOf<Mob>()
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
            1,
            mutableListOf(),
            mutableListOf(),
            mutableMapOf(
                Pair(Attribute.Hp, 20),
                Pair(Attribute.Mana, 100),
                Pair(Attribute.Moves, 100),
            ),
            mutableMapOf(),
            20,
            100,
            100,
            3001,
            Disposition.Standing,
        )
    }

    fun createMobEntity(
        name: String,
        description: String,
        brief: String,
        race: Race,
        roomId: Int,
        attributes: MutableMap<Attribute, Int>,
        affects: MutableMap<Affect, Int>,
    ): Mob {
        val entity = transaction {
            MobEntity.new {
                canonicalId = 0
                this.name = name
                this.description = description
                this.brief = brief
                this.race = race.toString()
                this.attributes = attributes
                this.affects = affects
                this.roomId = roomId
                hp = "1d1+1"
                mana = "1d1+1"
                moves = "1d1+1"
                disposition = Disposition.Standing.toString()
                maxInRoom = 1
                maxInGame = 1
                level = 1
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
        val hp = diceFromString(entity.hp)
        val mana = diceFromString(entity.mana)
        val moves = diceFromString(entity.moves)
        val attributes = entity.attributes.toMutableMap().also {
            it[Attribute.Hp] = hp
            it[Attribute.Mana] = mana
            it[Attribute.Moves] = moves
        }

        return Mob(
            entity.canonicalId,
            entity.name,
            entity.brief,
            entity.description,
            Race.valueOf(entity.race),
            entity.level,
            transaction { entity.items.map { itemService.createFromEntity(it) } }.toMutableList(),
            transaction { entity.equipped.map { itemService.createFromEntity(it) } }.toMutableList(),
            attributes,
            entity.affects,
            hp,
            mana,
            moves,
            entity.roomId,
            Disposition.valueOf(entity.disposition),
        )
    }

    fun getFightingMobs(): List<Mob> {
        return mobs.filter {
            it.target != null
        }
    }

    fun removeMob(mob: Mob) {
        mobs.remove(mob)
        mobsById[mob.id]?.remove(mob)
        mobRooms[mob.roomId]?.remove(mob)
        mob.roomId = 0
    }

    fun regen() {
        mobs.forEach {
            it.regen()
        }
    }

    fun affectDecay() {
        mobs.forEach { mob ->
            mob.affects.entries.removeIf {
                if (it.value == 0) {
                    return@removeIf true
                }
                if (it.value > 0) {
                    it.setValue(it.value - 1)
                }
                false
            }
        }
    }

    fun respawnMobs() {
        transaction {
            MobEntity.all().forEach { mob ->
                val inGame = mobsById[mob.canonicalId]?.size ?: 0
                val inRoom = mobRooms[mob.roomId]?.filter { it.roomId == mob.roomId }?.size ?: 0
                var allCountdown = mob.maxInGame - inGame
                var roomCountdown = mob.maxInRoom - inRoom
                while (allCountdown > 0 && roomCountdown > 0) {
                    hydrateMobEntity(mob)
                    allCountdown--
                    roomCountdown--
                }
            }
        }
    }

    private fun createMobInstance(id: Int): Mob? {
        return transaction { MobEntity.findById(id) }?.let {
            hydrateMobEntity(it)
        }
    }

    private fun hydrateMobEntity(mob: MobEntity): Mob {
        return mapMob(mob).also {
            addToMobs(it)
            addToMobRooms(it)
        }
    }

    private fun addToMobs(mob: Mob) {
        if (mobsById[mob.id] == null) {
            mobsById[mob.id] = mutableListOf()
        }
        mobsById[mob.id]!!.add(mob)
        mobs.add(mob)
    }

    private fun addToMobRooms(mob: Mob) {
        if (mobRooms[mob.roomId] == null) {
            mobRooms[mob.roomId] = mutableListOf()
        }
        mobRooms[mob.roomId]!!.add(mob)
    }
}
