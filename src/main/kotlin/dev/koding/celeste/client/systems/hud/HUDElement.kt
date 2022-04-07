package dev.koding.celeste.client.systems.hud

import dev.koding.celeste.client.utils.ui.BoundingBox
import net.minecraft.client.util.math.MatrixStack

@Suppress("unused")
abstract class HUDElement(
    val name: String,
    val description: String,
    var active: Boolean = false
) {

    var boundingBox = BoundingBox()

    // TODO: Storage for config and data using delegate

    open fun update() {}
    abstract fun render(stack: MatrixStack)

}