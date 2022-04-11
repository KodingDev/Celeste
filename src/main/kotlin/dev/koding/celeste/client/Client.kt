package dev.koding.celeste.client

import dev.koding.celeste.client.config.Config
import dev.koding.celeste.client.systems.command.CommandSystem
import dev.koding.celeste.client.systems.hud.HUDSystem
import dev.koding.celeste.client.systems.listener.Listeners
import dev.koding.celeste.client.systems.module.ModuleSystem
import dev.koding.celeste.client.utils.TickScheduler
import dev.koding.celeste.client.utils.mc
import mu.KotlinLogging
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import java.io.File
import kotlin.system.measureTimeMillis

@Environment(EnvType.CLIENT)
object Client {
    private val logger = KotlinLogging.logger { }

    private val container by lazy { FabricLoader.getInstance().getModContainer("celeste").get() }
    val storageDir by lazy { File(mc.runDirectory, name).apply { mkdirs() } }
    val config by lazy { Config() }

    val name: String by lazy { container.metadata.name }
    val version: String by lazy { container.metadata.version.friendlyString }

    @JvmStatic
    fun init() {
        logger.info { "Initializing Celeste Client" }
        val time = measureTimeMillis {
            logger.info { "Registering systems" }
            ModuleSystem.register()
            CommandSystem.setup()

            logger.info { "Registering listeners" }
            Listeners.register()
            HUDSystem.register()

            logger.info { "Starting tick scheduler" }
            TickScheduler.register()
        }
        logger.info { "Celeste Client initialized in ${time}ms" }
    }

    @JvmStatic
    fun id(name: String) = Identifier(container.metadata.id, name)
}