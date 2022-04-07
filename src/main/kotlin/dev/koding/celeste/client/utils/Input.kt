@file:Suppress("unused")

package dev.koding.celeste.client.utils

import org.lwjgl.glfw.GLFW

class Input(private val states: Array<Boolean>) {

    companion object {
        @JvmStatic
        val keyboard = Input(Array(512) { false })

        @JvmStatic
        val mouse = Input(Array(16) { false })
    }

    operator fun get(index: Int): Boolean {
        if (index < 0 || index >= states.size) return false
        return states[index]
    }

    operator fun set(index: Int, value: Boolean) {
        if (index < 0 || index >= states.size) return
        states[index] = value
    }

}

class KeyBind(var key: Int) {
    companion object {
        val empty = KeyBind(-1)
    }

    val name: String get() = GLFW.glfwGetKeyName(key, 0) ?: "Key $key"

    @get:JvmName("isPressed")
    val pressed
        get() = Input.keyboard[key]
}

object Mouse {
    val x: Double get() = mc.mouse.x
    val y: Double get() = mc.mouse.y

    val scaledX: Double get() = x / Window.scaleFactor
    val scaledY: Double get() = y / Window.scaleFactor
}