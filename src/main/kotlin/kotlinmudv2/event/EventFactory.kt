package kotlinmudv2.event

import kotlinmudv2.socket.Client

fun createGameLoopEvent(): Event<Any?> {
    return Event(EventType.GameLoop, null)
}

fun createTickEvent(): Event<Any?> {
    return Event(EventType.Tick, null)
}

fun createPulseEvent(): Event<Any?> {
    return Event(EventType.Pulse, null)
}

fun createClientConnectedEvent(client: Client): Event<Client> {
    return Event(EventType.ClientConnected, client)
}
