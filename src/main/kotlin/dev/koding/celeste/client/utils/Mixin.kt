package dev.koding.celeste.client.utils

import dev.koding.celeste.client.mixin.client.gui.hud.ChatHudAccessor
import net.minecraft.client.gui.hud.ChatHud

val ChatHud.accessor get() = (this as ChatHudAccessor)