package kotlinmudv2.action

import kotlinmudv2.mob.Mob

class Response(
    val mob: Mob,
    val toActionCreator: String,
    val actionStatus: ActionStatus = ActionStatus.Success,
)
