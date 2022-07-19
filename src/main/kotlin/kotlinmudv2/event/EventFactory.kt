package kotlinmudv2.event

import kotlinmudv2.socket.Client

fun createGameLoopEvent(): Event<Any?> {
    return Event(EventType.GameLoop, null)
}

fun createClientConnectedEvent(client: Client): Event<Client> {
    return Event(EventType.GameLoop, client)
}
