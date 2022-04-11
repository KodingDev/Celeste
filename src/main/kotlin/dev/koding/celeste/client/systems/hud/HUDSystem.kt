package dev.koding.celeste.client.systems.hud

import dev.koding.celeste.client.event.Listener
import dev.koding.celeste.client.event.impl.client.Render2DEvent
import dev.koding.celeste.client.event.listen
import dev.koding.celeste.client.utils.encoding.NBTCodable
import dev.koding.celeste.client.utils.encoding.nbt
import dev.koding.celeste.client.utils.encoding.read
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList

object HUDSystem : NBTCodable<HUDSystem>, Listener {
    private var built = false

    val window by lazy { Window(ElementaVersion.V1) }
    val elements by lazy { listOf<HUDElement>(TestElement(), TestElement().apply { boundingBox.x += 50 }) }

    init {
        listen<Render2DEvent> { event ->
            if (!built) {
                built = true
                rebuildWindow()
            }

            elements.forEach { it.update() }
            window.draw(UMatrixStack(event.stack))
        }
    }

    private fun rebuildWindow() {
        window.clearChildren()

        // TODO: Element toggling
        elements.forEach {
            it.constrain {
                x = it.boundingBox.x.pixels()
                y = it.boundingBox.y.pixels()
            } childOf window
        }
    }

    override fun toTag() = nbt {
        "elements" to elements.map { it.toTag() }
    }

    override fun fromTag(tag: NbtCompound) {
        tag.read<NbtList>("elements").forEach { nbt ->
            val name = (nbt as NbtCompound).read<String>("name")
            val element = elements.find { it.name == name }
                ?: throw IllegalArgumentException("Unknown HUD element: $name")
            element.fromTag(nbt)
        }
    }
}