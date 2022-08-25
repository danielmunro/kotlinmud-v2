package kotlinmudv2.mob

import kotlinmudv2.game.DamageType
import kotlinmudv2.socket.RoomMessage

data class Hit(
    val attacker: Mob,
    val defender: Mob,
    var damage: Int,
    val damageType: DamageType,
    val roomMessage: RoomMessage,
)
