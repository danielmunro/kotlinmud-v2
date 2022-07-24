package kotlinmudv2.mob

import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

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
    val items by ItemEntity optionalReferrersOn ItemTable.mob
}
