package kotlinmudv2.action

import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.socket.Client

class ContextService(
    private val mobService: MobService,
    private val actions: List<Action>,
) {
    fun findActionForInput(client: Client, input: String): ActionWithContext? {
        val parts = input.split(" ")
        val context = mutableMapOf<Int, Any>()
        return actions.find { action ->
            var i = 0
            action.syntax.map SyntaxFind@{ syntax ->
                if (i >= parts.size) {
                    return@SyntaxFind false
                }
                val index = i
                i++
                when (syntax) {
                    Syntax.Command -> action.command.value.startsWith(parts[0])
                    Syntax.MobInRoom -> {
                        val mobName = parts[index]
                        findMobInRoom(client.mob.roomId, mobName)?.let {
                            context[index] = it
                            true
                        } ?: false
                    }
                    Syntax.ItemInInventory -> false
                    Syntax.ItemInRoom -> false
                }
            }.filter { it }.size == action.syntax.size
        }?.let {
            ActionWithContext(it, context)
        }
    }

    private fun findMobInRoom(roomId: Int, mobName: String): Mob? {
        return mobService.getMobsForRoom(roomId).find { it.name.startsWith(mobName) }
    }
}
