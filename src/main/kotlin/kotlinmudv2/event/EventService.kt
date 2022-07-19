package kotlinmudv2.event

class EventService {
    lateinit var observers: Map<EventType, List<Observer>>

    suspend fun <T> publish(event: Event<T>) {
        (observers[event.eventType] ?: return).forEach {
            it.invokeAsync(event)
        }
    }
}