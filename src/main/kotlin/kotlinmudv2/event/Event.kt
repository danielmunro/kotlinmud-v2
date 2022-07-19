package kotlinmudv2.event

open class Event<T>(val eventType: EventType, val subject: T)
