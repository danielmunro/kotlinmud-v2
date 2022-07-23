package kotlinmudv2.observer

import kotlinmudv2.action.*
import kotlinmudv2.event.Event
import kotlinmudv2.socket.Client
import kotlinmudv2.socket.SocketService
import kotlinx.coroutines.flow.asFlow

class ProcessClientBufferObserver(
    private val socketService: SocketService,
    private val actionService: ActionService,
    private val contextService: ContextService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        socketService.getClientsWithBuffers().asFlow().collect {
            processRequest(it)
        }
    }

    fun handleRequest(client: Client, input: String): Response {
        val response = contextService.findActionForInput(client, input)?.let {
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
}
