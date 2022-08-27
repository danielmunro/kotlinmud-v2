package kotlinmudv2.action

import kotlinmudv2.mob.Mob

class Response(
    val mob: Mob,
    val toActionCreator: String,
    val toRoom: String? = null,
    val target: Mob? = null,
    val toTarget: String? = null,
    val actionStatus: ActionStatus = ActionStatus.Success,
)
