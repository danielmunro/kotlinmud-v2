package kotlinmudv2.action

import kotlinmudv2.mob.Mob

class Response (
    val mob: Mob,
    val actionStatus: ActionStatus,
    val toActionCreator: String,
    val toTarget: String? = null,
    val toObservers: String? = null,
)