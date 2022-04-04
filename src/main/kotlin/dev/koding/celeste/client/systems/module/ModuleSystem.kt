package dev.koding.celeste.client.systems.module

import dev.koding.celeste.client.event.Listener
import dev.koding.celeste.client.event.impl.client.KeyboardEvent
import dev.koding.celeste.client.event.listen
import dev.koding.celeste.client.utils.KeyBind
import dev.koding.celeste.client.utils.mc
import net.minecraft.item.Items
import org.lwjgl.glfw.GLFW

object ModuleSystem : Listener {

    val modules = listOf<Module>(
        TestModule // Misc
    )

    init {
        listen<KeyboardEvent> { event ->
            if (mc.currentScreen != null) return@listen
            if (event.action != KeyboardEvent.Action.PRESS) return@listen
            modules
                .filter { it.key?.key == event.key }
                .forEach { it.toggle() }
        }
    }

}

object TestModule :
    Module(ModuleCategory.MISC, Items.NETHER_STAR, "Test", "Test ha ha ha !", key = KeyBind(GLFW.GLFW_KEY_T)) {
    private var awesome = true

    init {
        listen<KeyboardEvent> {
            println("TestModule: $it")
        }

        config {
            switch(::awesome, "Awesome", "Some awesome stuff")
        }
    }

    override fun onEnable() {
        println("Test Module Enabled")
    }

    override fun onDisable() {
        println("Test Module Disabled")
    }
}