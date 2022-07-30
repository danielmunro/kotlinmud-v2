package kotlinmudv2.observer

import kotlinmudv2.action.ActionService
import kotlinmudv2.action.ActionStatus
import kotlinmudv2.action.ContextService
import kotlinmudv2.action.Response
import kotlinmudv2.event.Event
import kotlinmudv2.socket.AuthService
import kotlinmudv2.socket.Client
import kotlinmudv2.socket.SocketService
import kotlinx.coroutines.flow.asFlow

class ProcessClientBufferObserver(
    private val socketService: SocketService,
    private val actionService: ActionService,
    private val contextService: ContextService,
    private val authService: AuthService,
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        socketService.getClientsWithBuffers().asFlow().collect { client ->
            client.mob?.let { processRequest(client) } ?: authService.handleInput(client)
        }
    }

    fun handleRequest(client: Client, input: String): Response {
        val ctx = contextService.findActionForInput(client, input)

        if (ctx != null && !ctx.action.dispositions.contains(client.mob!!.disposition)) {
            return Response(
                client.mob!!,
                "you are ${client.mob!!.disposition.toString().lowercase()} and cannot do that.",
            )
        }

        return ctx?.execute(actionService, client.mob!!, input)
            ?: Response(client.mob!!, "what was that?", ActionStatus.Error)
    }

    private fun processRequest(client: Client) {
        if (client.isDelayed()) {
            return
        }

        client.shiftInput().also {
            handleRequest(client, it).also {
                response ->
                client.write(response.toActionCreator)
            }
        }
    }
}
