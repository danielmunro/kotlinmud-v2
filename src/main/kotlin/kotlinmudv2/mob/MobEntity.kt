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
class AffectToken : TypeToken<Map<Affect, Int>>()

class MobEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MobEntity>(MobTable)
    var name by MobTable.name
    var brief by MobTable.brief
    var description by MobTable.description
    var hp by MobTable.hp
    var mana by MobTable.mana
    var moves by MobTable.moves
    var maxHp by MobTable.maxHp
    var maxMana by MobTable.maxMana
    var maxMoves by MobTable.maxMoves
    var roomId by MobTable.roomId
    var race by MobTable.race
    val items by ItemEntity optionalReferrersOn ItemTable.mobInventory
    val equipped by ItemEntity optionalReferrersOn ItemTable.mobEquipped
    var attributes: MutableMap<Attribute, Int> by MobTable.attributes.transform(
        { Gson().toJson(it) },
        { Gson().fromJson(it, AttributeToken().type) },
    )
    var affects: MutableMap<Affect, Int> by MobTable.affects.transform(
        { Gson().toJson(it) },
        { Gson().fromJson(it, AffectToken().type)}
    )
}
