package kotlinmudv2.action

import kotlinmudv2.mob.Mob

fun errorResponse(mob: Mob, message: String): Response {
    return Response(
        mob,
        message,
        null,
        null,
        ActionStatus.Error,
    )
}

fun failResponse(mob: Mob, message: String): Response {
    return Response(
        mob,
        message,
        null,
        null,
        ActionStatus.Failure,
    )
}
