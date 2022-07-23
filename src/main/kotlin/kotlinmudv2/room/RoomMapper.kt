package kotlinmudv2.room

fun mapRoom(entity: RoomEntity): Room {
    return Room(
        entity.id.value,
        entity.name,
        entity.description,
        entity.northId,
        entity.southId,
        entity.eastId,
        entity.westId,
        entity.upId,
        entity.downId,
    )
}
