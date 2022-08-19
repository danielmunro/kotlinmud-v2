package kotlinmudv2.mob

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinmudv2.game.Affect
import kotlinmudv2.game.Attribute
import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AttributeToken : TypeToken<Map<Attribute, Int>>()
class AffectToken : TypeToken<MutableList<Affect>>()
class FlagsToken : TypeToken<List<MobFlag>>()
class RaceToken : TypeToken<Race>()
val gson = Gson()

class MobEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobEntity>(MobTable)
    var canonicalId by MobTable.canonicalId
    var name by MobTable.name
    var brief by MobTable.brief
    var description by MobTable.description
    var hp by MobTable.hp
    var mana by MobTable.mana
    var moves by MobTable.moves
    var roomId by MobTable.roomId
    var race: Race by MobTable.race.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, RaceToken().type) }
    )
    var disposition by MobTable.disposition
    var maxInRoom by MobTable.maxInRoom
    var maxInGame by MobTable.maxInGame
    var level by MobTable.level
    var coins by MobTable.coins
    val items by ItemEntity optionalReferrersOn ItemTable.mobInventory
    val equipped by ItemEntity optionalReferrersOn ItemTable.mobEquipped
    var attributes: MutableMap<Attribute, Int> by MobTable.attributes.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, AttributeToken().type) },
    )
    var affects: MutableList<Affect> by MobTable.affects.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, AffectToken().type) }
    )
    var flags: MutableList<MobFlag> by MobTable.flags.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, FlagsToken().type) }
    )
}
