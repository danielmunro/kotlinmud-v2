package kotlinmudv2.event.observer

import kotlinmudv2.action.Action
import kotlinmudv2.action.ActionService
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
            actions.find {
                it.command.value == input
            }?.let {
                it.execute(
                    ActionService(),
                    client.mob!!,
                    input,
                ).toActionCreator
            } ?: "What was that?"
        )
    }
}
