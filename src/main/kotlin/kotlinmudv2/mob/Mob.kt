package kotlinmudv2.mob

class Mob(
    private val entity: MobEntity,
) {
    var hp = entity.hp
    var mana = entity.mana
    var moves = entity.moves
    var roomId = entity.roomId
}