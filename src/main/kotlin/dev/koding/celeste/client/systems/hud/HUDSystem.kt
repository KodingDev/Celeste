package dev.koding.celeste.client.systems.hud

import dev.koding.celeste.client.utils.encoding.NBTCodable
import dev.koding.celeste.client.utils.encoding.nbt
import dev.koding.celeste.client.utils.encoding.read
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList

object HUDSystem : NBTCodable<HUDSystem> {
    val elements = listOf<HUDElement>(TestElement(), TestElement().apply { boundingBox.x += 50 })

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