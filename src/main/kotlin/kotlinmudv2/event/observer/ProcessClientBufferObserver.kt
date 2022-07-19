package kotlinmudv2.event.observer

import kotlinmudv2.event.Event
import kotlinmudv2.socket.Client
import kotlinmudv2.socket.SocketService
import kotlinx.coroutines.flow.asFlow

class ProcessClientBufferObserver(private val socketService: SocketService) : Observer {
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
        // echo
        client.writePrompt(input)
    }
}
