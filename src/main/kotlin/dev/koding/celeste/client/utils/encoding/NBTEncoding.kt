package dev.koding.celeste.client.utils.encoding

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtList
import java.io.ByteArrayOutputStream

interface NBTCodable<T> {
    fun toTag(): NbtCompound
    fun fromTag(tag: NbtCompound)
}

object NBTEncoding {
    fun encode(compound: NbtCompound): ByteArray = ByteArrayOutputStream().use {
        NbtIo.writeCompressed(compound, it)
        it.toByteArray()
    }

    fun decode(bytes: ByteArray): NbtCompound = NbtIo.readCompressed(bytes.inputStream())
}

/**
 * NBT builder DSL
 */
@DslMarker
annotation class NBTDsl

@NBTDsl
class NBTBuilder {
    private val data = mutableMapOf<String, Any>()

    operator fun set(key: String, value: Any) {
        data[key] = value
    }

    infix fun String.to(value: Any) = set(this, value)

    fun build(): NbtCompound = NbtCompound().apply {
        data.forEach { (name, value) ->
            when (value) {
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Long -> putLong(name, value)
                is Double -> putDouble(name, value)
                is Float -> putFloat(name, value)
                is Boolean -> putBoolean(name, value)
                is ByteArray -> putByteArray(name, value)
                is NbtElement -> put(name, value)
                is NBTBuilder -> put(name, value.build())
                is ArrayList<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val type = value as? ArrayList<out NbtElement>
                        ?: throw IllegalArgumentException("Invalid list type")
                    put(name, nbtArray(*type.toTypedArray()))
                }
                else -> throw IllegalArgumentException("Unsupported type: ${value.javaClass}")
            }
        }
    }
}

@NBTDsl
fun nbt(block: NBTBuilder.() -> Unit): NbtCompound = NBTBuilder().apply(block).build()

@NBTDsl
fun nbtArray(vararg elements: NbtElement): NbtList = NbtList().apply { addAll(elements.toList()) }

@Suppress("UNCHECKED_CAST")
fun <T> NbtCompound.read(key: String): T = get(key) as? T ?: throw IllegalArgumentException("Invalid type")