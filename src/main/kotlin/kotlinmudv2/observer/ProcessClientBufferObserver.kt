package kotlinmudv2.observer

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionService
import kotlinmudv2.action.Syntax
import kotlinmudv2.event.Event
import kotlinmudv2.socket.Client
import kotlinmudv2.socket.SocketService
import kotlinx.coroutines.flow.asFlow

class ProcessClientBufferObserver(
    private val socketService: SocketService,
    private val actions: List<Action>,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        socketService.getClientsWithBuffers().asFlow().collect {
            processRequest(it)
        }
    }
    private fun processRequest(client: Client) {
        if (client.isDelayed()) {
            return
        }

        client.shiftInput().also { handleRequest(client, it) }
    }

    private fun handleRequest(client: Client, input: String) {
        client.writePrompt(
            findActionForInput(input)?.let {
                it.execute(
                    ActionService(),
                    client.mob!!,
                    input,
                ).toActionCreator
            } ?: "What was that?"
        )
    }

    private fun findActionForInput(input: String): Action? {
        val parts = input.split(" ")
        return actions.find { action ->
            action.syntax.find { syntax ->
                when (syntax) {
                    Syntax.Command -> parts[0] == action.command.value
                }
            } != null
        }
    }
}
