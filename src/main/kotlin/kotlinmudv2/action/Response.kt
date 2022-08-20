package kotlinmudv2.action

import kotlinmudv2.mob.Mob

class Response(
    val mob: Mob,
    val toActionCreator: String,
    val toTarget: String? = null,
    val toRoom: String? = null,
    val actionStatus: ActionStatus = ActionStatus.Success,
)
