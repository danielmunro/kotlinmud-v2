package kotlinmudv2.action.skills

import kotlinmudv2.action.Response
import kotlinmudv2.mob.Mob

fun tooTired(mob: Mob): Response {
    return Response(
        mob,
        "you are too tired and cannot do that",
    )
}
