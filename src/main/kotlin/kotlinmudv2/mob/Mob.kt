package kotlinmudv2.mob

class Mob(
    val id: Int,
    val name: String,
    val description: String,
    var hp: Int,
    var maxHp: Int,
    var mana: Int,
    var maxMana: Int,
    var moves: Int,
    var maxMoves: Int,
    var roomId: Int,
)