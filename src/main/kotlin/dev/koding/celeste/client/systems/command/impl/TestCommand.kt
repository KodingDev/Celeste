package dev.koding.celeste.client.systems.command.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.koding.celeste.client.systems.command.Command
import dev.koding.celeste.client.systems.hud.EditHUDScreen
import dev.koding.celeste.client.utils.TickScheduler
import dev.koding.celeste.client.utils.mc
import net.minecraft.command.CommandSource

object TestCommand : Command("test", "Debug command", "test") {
    override fun build(builder: LiteralArgumentBuilder<CommandSource>) {
        builder.executes {
            TickScheduler.schedule { mc.setScreen(EditHUDScreen()) }
            1
        }
    }

}