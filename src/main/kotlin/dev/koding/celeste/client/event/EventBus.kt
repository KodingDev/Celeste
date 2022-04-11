package dev.koding.celeste.client.event

object EventBus {
    private val listeners = arrayListOf<Any>()
    val subscriptions = hashMapOf<Class<out Event>, ArrayList<EventSubscription<*>>>()

    @JvmStatic
    @Suppress("MemberVisibilityCanBePrivate", "unused")
    fun <T : Event> post(event: T): T {
        subscriptions
            .filter { (klass) -> klass.isInstance(event) }
            .flatMap { (_, subscriptions) -> subscriptions }
            .filter { it.owner in listeners }
            .forEach {
                @Suppress("UNCHECKED_CAST")
                (it.listener as (Event) -> Unit)(event)
            }
        return event
    }

    inline fun <reified T : Event> listen(instance: Any, noinline listener: (T) -> Unit) =
        subscriptions.getOrPut(T::class.java) { arrayListOf() }.add(EventSubscription(instance, listener))

    private fun subscribe(instance: Any) {
        listeners += instance
    }

    private fun unsubscribe(instance: Any) {
        listeners -= instance
    }

    operator fun plusAssign(instance: Any) = subscribe(instance)
    operator fun minusAssign(instance: Any) = unsubscribe(instance)
}

data class EventSubscription<T : Event>(
    val owner: Any,
    val listener: (T) -> Unit
)

interface Listener {
    fun register() = EventBus.plusAssign(this)
    fun unregister() = EventBus.minusAssign(this)
}

inline fun <reified T : Event> Listener.listen(noinline listener: (T) -> Unit) = EventBus.listen(this, listener)