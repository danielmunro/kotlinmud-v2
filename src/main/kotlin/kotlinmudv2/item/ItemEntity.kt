package kotlinmudv2.item

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinmudv2.game.Attribute
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Token : TypeToken<Map<Attribute, Int>>()

class ItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemEntity>(ItemTable)
    var name by ItemTable.name
    var brief by ItemTable.brief
    var description by ItemTable.description
    var itemType by ItemTable.itemType
    var position by ItemTable.position
    var mobInventory by ItemTable.mobInventory
    var mobEquipped by ItemTable.mobEquipped
    var room by ItemTable.room
    var attributes: MutableMap<Attribute, Int> by ItemTable.attributes.transform(
        { Gson().toJson(it) },
        { Gson().fromJson(it, Token().type) },
    )
}
