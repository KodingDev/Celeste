package dev.koding.celeste.client.systems.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.koding.celeste.client.utils.mc
import net.minecraft.client.network.ClientCommandSource
import net.minecraft.command.CommandSource

object CommandSystem {

    private val commands = arrayListOf<Command>(BasedCommand)

    @JvmStatic
    val dispatcher = CommandDispatcher<CommandSource>()

    @JvmStatic
    val commandSource = ClientCommandSource(null, mc)

    @JvmStatic
    @Throws(CommandSyntaxException::class)
    fun dispatch(command: String) = dispatcher.execute(dispatcher.parse(command, commandSource))

    fun setup() {
        commands.forEach { it.register(dispatcher) }
    }

}

object BasedCommand : Command("Based", "Swag !", "based", "aliased") {
    override fun build(builder: LiteralArgumentBuilder<CommandSource>) {
        builder
            .then(literal("based").executes {
                println("based")
                1
            })
    }
}