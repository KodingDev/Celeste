package dev.koding.celeste.client.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import java.awt.Color

val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }

object Chat {
    private val prefix = LiteralText("")
        .append(LiteralText("CELESTE").styled { it.withColor(Colors.primary.rgb).withBold(true) })
        .append(LiteralText(" » ").styled { it.withColor(Colors.gray.rgb) })

    @JvmStatic
    @Suppress("KotlinConstantConditions")
    fun send(message: Text, prefixed: Boolean = true, id: Int = 0) = mc.inGameHud.chatHud.accessor.add(
        if (prefixed) LiteralText("").apply { if (prefixed) append(prefix) }.append(message)
        else message,
        id
    )

    @JvmStatic
    fun error(message: String) = send(LiteralText(message).styled { it.withColor(Colors.error.rgb) })

    @JvmStatic
    fun info(message: String, id: Int = 0) =
        send(LiteralText(message).styled { it.withColor(Colors.info.rgb) }, id = id)
}

object Colors {
    val primary = Color(0xf48fb1)
    val error = Color(0xF44336)
    val info = Color(0xfce4ec)
    val gray = Color(0x9e9e9e)
}