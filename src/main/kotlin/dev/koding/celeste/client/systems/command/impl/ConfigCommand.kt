package dev.koding.celeste.client.systems.command.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.koding.celeste.client.Client
import dev.koding.celeste.client.systems.command.Command
import dev.koding.celeste.client.utils.TickScheduler
import dev.koding.celeste.client.utils.mc
import net.minecraft.command.CommandSource

object ConfigCommand : Command("Config", "Open the config GUI", "config") {
    override fun build(builder: LiteralArgumentBuilder<CommandSource>) {
        builder.executes {
            TickScheduler.schedule { mc.setScreen(Client.config.gui()) }
            1
        }
    }
}