package kotlinmudv2.action

import kotlinmudv2.mob.Mob

class ActionWithContext(
    val action: Action,
    private val context: Map<Int, Any>
) {
    fun execute(actionService: ActionService, mob: Mob, input: String): Response {
        return action.execute(Request(actionService, mob, context, input))
    }
}
