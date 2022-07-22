package kotlinmudv2.room

class Room (roomEntity: RoomEntity) {
    val id = roomEntity.id
    var name = roomEntity.name
    var description = roomEntity.description
    var northId = roomEntity.northId
    var southId = roomEntity.southId
    var eastId = roomEntity.eastId
    var westId = roomEntity.westId
    var upId = roomEntity.upId
    var downId = roomEntity.downId
}