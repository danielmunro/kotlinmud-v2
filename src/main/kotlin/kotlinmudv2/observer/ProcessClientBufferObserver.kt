package kotlinmudv2.observer

import kotlinmudv2.action.*
import kotlinmudv2.event.Event
import kotlinmudv2.mob.Mob
import kotlinmudv2.mob.MobService
import kotlinmudv2.socket.Client
import kotlinmudv2.socket.SocketService
import kotlinx.coroutines.flow.asFlow

class ProcessClientBufferObserver(
    private val socketService: SocketService,
    private val actionService: ActionService,
    private val mobService: MobService,
    private val actions: List<Action>,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        socketService.getClientsWithBuffers().asFlow().collect {
            processRequest(it)
        }
    }

    fun handleRequest(client: Client, input: String): Response {
        val response = findActionForInput(client, input)?.let {
            it.execute(
                actionService,
                client.mob,
                input,
            )
        } ?: Response(
            client.mob,
            ActionStatus.Error,
            "What was that?",
        )
        client.writePrompt(response.toActionCreator)
        return response
    }

    private fun processRequest(client: Client) {
        if (client.isDelayed()) {
            return
        }

        client.shiftInput().also { handleRequest(client, it) }
    }

    private fun findActionForInput(client: Client, input: String): ActionWithContext? {
        val parts = input.split(" ")
        val context = mutableMapOf<Int, Any>()
        return actions.find { action ->
            var i = 0
            action.syntax.map SyntaxFind@ { syntax ->
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
