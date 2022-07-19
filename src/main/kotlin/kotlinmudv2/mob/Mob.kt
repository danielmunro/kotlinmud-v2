package kotlinmudv2.mob

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Mob(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Mob>(MobTable)
    var name by MobTable.name
    var brief by MobTable.brief
    var description by MobTable.description
    var roomId by MobTable.roomId
}
