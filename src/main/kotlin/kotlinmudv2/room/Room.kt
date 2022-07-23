package kotlinmudv2.room

class Room (
    val id: Int,
    val name: String,
    val description: String,
    var northId: Int?,
    var southId: Int?,
    var eastId: Int?,
    var westId: Int?,
    var upId: Int?,
    var downId: Int?,
)