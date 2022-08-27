package kotlinmudv2.item

import kotlinmudv2.game.AffectType
import kotlinmudv2.game.Attribute
import kotlinmudv2.game.DamageType

class Item(
    val id: Int,
    val name: String,
    val description: String,
    val brief: String,
    val itemType: ItemType,
    val material: String,
    val level: Int,
    val value: Int,
    val flags: MutableList<ItemFlag>,
    val attributes: MutableMap<Attribute, Int>,
    val affects: MutableMap<AffectType, Int>,
    val position: Position? = null,
    val weaponType: WeaponType? = null,
    val damageType: DamageType? = null,
    val damageRolls: Int? = null,
    val damageDice: Int? = null,
    val attackVerb: String? = null,
) {
    override fun toString(): String {
        return name
    }
}
