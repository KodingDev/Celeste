package dev.koding.celeste.client.systems.listener

import dev.koding.celeste.client.Client
import dev.koding.celeste.client.event.Listener
import dev.koding.celeste.client.event.impl.client.KeyboardEvent
import dev.koding.celeste.client.event.listen
import dev.koding.celeste.client.systems.hud.EditHUDScreen
import dev.koding.celeste.client.utils.mc
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import org.lwjgl.glfw.GLFW

object ConfigListener : Listener {
    init {
        listen<KeyboardEvent> {
            if (mc.currentScreen !is TitleScreen && mc.currentScreen !is MultiplayerScreen) return@listen
            when (it.key) {
                GLFW.GLFW_KEY_RIGHT_SHIFT -> mc.executeSync { mc.setScreen(Client.config.gui()) }
                GLFW.GLFW_KEY_H -> mc.executeSync { mc.setScreen(EditHUDScreen) }
            }
        }
    }
}