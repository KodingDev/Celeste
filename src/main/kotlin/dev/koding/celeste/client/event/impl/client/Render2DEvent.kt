package dev.koding.celeste.client.event.impl.client

import dev.koding.celeste.client.event.Event
import net.minecraft.client.util.math.MatrixStack

data class Render2DEvent(
    val partialTicks: Float,
    val stack: MatrixStack
) : Event