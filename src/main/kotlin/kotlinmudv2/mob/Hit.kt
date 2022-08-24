package kotlinmudv2.mob

import kotlinmudv2.game.DamageType

data class Hit(
    val attacker: Mob,
    val defender: Mob,
    var damage: Int,
    val damageType: DamageType,
)
