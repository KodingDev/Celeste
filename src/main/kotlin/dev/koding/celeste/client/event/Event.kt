package dev.koding.celeste.client.event

interface Event

open class CancelableEvent : Event {
    @get:JvmName("isCancelled")
    var canceled = false
        private set

    @Suppress("unused")
    fun cancel() {
        canceled = true
    }
}