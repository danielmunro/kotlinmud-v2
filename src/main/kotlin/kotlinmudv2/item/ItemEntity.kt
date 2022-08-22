package kotlinmudv2.item

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.game.DamageType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Token : TypeToken<Map<Attribute, Int>>()
class AffectToken : TypeToken<Map<AffectType, Int>>()
class ItemFlagToken : TypeToken<List<ItemFlag>>()
val gson = Gson()

class ItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemEntity>(ItemTable)
    var name by ItemTable.name
    var brief by ItemTable.brief
    var description by ItemTable.description
    var itemType by ItemTable.itemType
    var position by ItemTable.position.transform(
        { it.toString() },
        { it?.let { Position.valueOf(it) } }
    )
    var mobInventory by ItemTable.mobInventory
    var mobEquipped by ItemTable.mobEquipped
    var level by ItemTable.level
    var value by ItemTable.value
    var weight by ItemTable.weight
    var material by ItemTable.material
    var room by ItemTable.room
    var attackVerb by ItemTable.attackVerb
    var damageType by ItemTable.damageType.transform(
        { it.toString() },
        { it?.let { DamageType.valueOf(it) } },
    )
    var weaponType by ItemTable.weaponType.transform(
        { it.toString() },
        { it?.let { WeaponType.valueOf(it) } },
    )
    var damageRolls by ItemTable.damageRolls
    var damageDice by ItemTable.damageDice
    var attributes: MutableMap<Attribute, Int> by ItemTable.attributes.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, Token().type) },
    )
    var affects: MutableMap<AffectType, Int> by ItemTable.affects.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, AffectToken().type) }
    )
    var flags: MutableList<ItemFlag> by ItemTable.flags.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, ItemFlagToken().type) },
    )
}
