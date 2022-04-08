package dev.koding.celeste.client.systems.hud

import dev.koding.celeste.client.utils.encoding.NBTCodable
import dev.koding.celeste.client.utils.encoding.nbt
import dev.koding.celeste.client.utils.encoding.read
import dev.koding.celeste.client.utils.ui.BoundingBox
import gg.essential.elementa.UIComponent
import net.minecraft.nbt.NbtCompound

@Suppress("unused")
abstract class HUDElement(
    val name: String,
    val description: String,
    var active: Boolean = false
) : NBTCodable<HUDElement>, UIComponent() {

    var boundingBox = BoundingBox()

    // TODO: Storage for config and data using delegate

    open fun update() {
        boundingBox.width = getWidth().toDouble()
        boundingBox.height = getHeight().toDouble()
    }

    override fun toTag() = nbt {
        "name" to name
        "x" to boundingBox.xPercent
        "y" to boundingBox.yPercent
    }

    override fun fromTag(tag: NbtCompound) {
        boundingBox.xPercent = tag.read("x")
        boundingBox.yPercent = tag.read("y")
    }

}