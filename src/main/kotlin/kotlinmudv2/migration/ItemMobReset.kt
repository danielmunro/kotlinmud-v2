package kotlinmudv2.migration

class ItemMobReset(
    val mobId: Int,
    val itemId: Int,
    val maxToInventory: Int,
    val maxInGame: Int,
    val isEquipped: Boolean = false,
)
