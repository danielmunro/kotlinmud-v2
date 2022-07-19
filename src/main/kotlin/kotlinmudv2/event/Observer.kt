package kotlinmudv2.event

interface Observer {
    suspend fun <T> invokeAsync(event: Event<T>)
}
