package dev.koding.celeste.client.event.impl.world

import dev.koding.celeste.client.event.Event

data class TickEvent(
    val phase: Phase
) : Event {
    enum class Phase {
        PRE,
        POST
    }
}