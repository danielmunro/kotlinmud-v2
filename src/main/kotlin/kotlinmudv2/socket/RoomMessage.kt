package kotlinmudv2.socket

import kotlinmudv2.mob.Mob

class RoomMessage(
    val actionCreator: Mob,
    val toActionCreator: String,
    val toObservers: String,
    val target: Mob? = null,
    val toTarget: String? = null,
)
