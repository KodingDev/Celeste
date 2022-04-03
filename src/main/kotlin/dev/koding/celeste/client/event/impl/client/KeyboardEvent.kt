package dev.koding.celeste.client.event.impl.client

import dev.koding.celeste.client.event.CancelableEvent
import org.lwjgl.glfw.GLFW

data class KeyboardEvent(
    val key: Int,
    val modifiers: Int,
    val action: Action
) : CancelableEvent() {
    enum class Action {
        PRESS,
        RELEASE,
        REPEAT;

        companion object {
            @JvmStatic
            @JvmName("parse")
            operator fun get(action: Int) = when (action) {
                GLFW.GLFW_PRESS -> PRESS
                GLFW.GLFW_RELEASE -> RELEASE
                else -> REPEAT
            }
        }
    }
}