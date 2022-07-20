package kotlinmudv2.mob

class Mob(
    private val entity: MobEntity,
) {
    val hp = entity.hp
    val mana = entity.mana
    val moves = entity.moves
    val roomId = entity.roomId
}