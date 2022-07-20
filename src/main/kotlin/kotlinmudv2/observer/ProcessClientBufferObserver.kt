package kotlinmudv2.observer

import kotlinmudv2.action.*
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

    fun handleRequest(client: Client, input: String): Response {
        val response = findActionForInput(input)?.let {
            it.execute(
                ActionService(),
                client.mob!!,
                input,
            )
        } ?: Response(
            client.mob!!,
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
