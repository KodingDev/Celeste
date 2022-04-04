package dev.koding.celeste.client.systems.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.command.CommandSource

@Suppress("unused")
abstract class Command(
    val name: String,
    val description: String,
    private vararg val aliases: String
) {

    protected fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<CommandSource, T> =
        RequiredArgumentBuilder.argument(name, type)

    protected fun literal(literal: String): LiteralArgumentBuilder<CommandSource> =
        LiteralArgumentBuilder.literal(literal)

    abstract fun build(builder: LiteralArgumentBuilder<CommandSource>)

    fun register(dispatcher: CommandDispatcher<CommandSource>) =
        aliases.forEach { dispatcher.register(literal(it).apply { build(this) }) }

}