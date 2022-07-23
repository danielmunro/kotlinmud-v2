package kotlinmudv2.mob

fun mapMob(entity: MobEntity): Mob {
    return Mob(
        entity.id.value,
        entity.name,
        entity.description,
        entity.hp,
        entity.maxHp,
        entity.mana,
        entity.maxMana,
        entity.moves,
        entity.maxMoves,
        entity.roomId,
    )
}