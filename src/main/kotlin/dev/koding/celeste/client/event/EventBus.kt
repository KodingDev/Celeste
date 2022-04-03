package dev.koding.celeste.client.event

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

object EventBus {
    private val eventFlow = MutableSharedFlow<Event>()
    val events = eventFlow.asSharedFlow()

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val subscriptions = arrayListOf<Any>()

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    suspend fun <T : Event> post(event: T): T {
        eventFlow.emit(event)
        return event
    }

    @JvmStatic
    @Suppress("unused")
    fun <T : Event> postBlocking(event: T) = runBlocking { post(event) }

    inline fun <reified T : Event> listen(instance: Any, noinline listener: (T) -> Unit) =
        scope.launch {
            events
                .filterIsInstance<T>()
                .filter { instance in subscriptions }
                .collectLatest { listener(it) }
        }

    private fun subscribe(instance: Any) {
        subscriptions += instance
    }

    private fun unsubscribe(instance: Any) {
        subscriptions -= instance
    }

    operator fun plusAssign(instance: Any) = subscribe(instance)
    operator fun minusAssign(instance: Any) = unsubscribe(instance)
}

interface Listener {
    fun register() = EventBus.plusAssign(this)
    fun unregister() = EventBus.minusAssign(this)
}

inline fun <reified T : Event> Listener.listen(noinline listener: (T) -> Unit) = EventBus.listen(this, listener)