package dev.koding.celeste.client.utils

import dev.koding.celeste.client.event.Listener
import dev.koding.celeste.client.event.impl.world.TickEvent
import dev.koding.celeste.client.event.listen

object TickScheduler : Listener {

    private val tasks = mutableListOf<() -> Unit>()

    init {
        listen<TickEvent> {
            if (it.phase != TickEvent.Phase.POST) return@listen
            tasks.forEach { mc.executeSync { it() } }
            tasks.clear()
        }
    }

    fun schedule(task: () -> Unit) = tasks.add(task)

}