package kotlinmudv2.room

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinmudv2.item.ItemEntity
import kotlinmudv2.item.ItemTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ExitsToken : TypeToken<MutableList<Exit>>()
val gson = Gson()

class RoomEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomEntity>(RoomTable)
    var name by RoomTable.name
    var description by RoomTable.description
    val items by ItemEntity optionalReferrersOn ItemTable.room
    var exits: MutableList<Exit> by RoomTable.exits.transform(
        { gson.toJson(it) },
        { gson.fromJson(it, ExitsToken().type) },
    )

    fun setExit(direction: Direction, roomId: Int, keyword: String? = null, status: ExitStatus? = null) {
        val filtered = exits.filter { it.direction == direction }
        val added = filtered.toMutableList()
        added.add(Exit(direction, roomId, keyword, status))
        exits = added
    }
}
