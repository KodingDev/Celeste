package dev.koding.celeste.client.systems.module

import dev.koding.celeste.client.event.Listener
import dev.koding.celeste.client.event.impl.client.KeyboardEvent
import dev.koding.celeste.client.event.listen
import dev.koding.celeste.client.utils.KeyBind
import net.minecraft.item.Items
import org.lwjgl.glfw.GLFW

object ModuleSystem : Listener {

    private val modules = listOf<Module>(
        TestModule // Misc
    )

    init {
        listen<KeyboardEvent> { event ->
            if (event.action != KeyboardEvent.Action.PRESS) return@listen
            modules
                .filter { it.key?.key == event.key }
                .forEach { it.toggle() }
        }
    }

}

object TestModule :
    Module(ModuleCategory.MISC, Items.NETHER_STAR, "test", "Test Module", key = KeyBind(GLFW.GLFW_KEY_T)) {
    init {
        listen<KeyboardEvent> {
            println("TestModule: $it")
        }
    }

    override fun onEnable() {
        println("Test Module Enabled")
    }

    override fun onDisable() {
        println("Test Module Disabled")
    }
}