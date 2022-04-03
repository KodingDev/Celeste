package dev.koding.celeste.client

import dev.koding.celeste.client.config.Config
import dev.koding.celeste.client.systems.listeners.Listeners
import dev.koding.celeste.client.systems.module.ModuleSystem
import dev.koding.celeste.client.utils.mc
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.util.Identifier
import java.io.File

@Environment(EnvType.CLIENT)
class CelesteClient : ClientModInitializer, ICelesteClient {
    companion object {
        lateinit var instance: CelesteClient
            private set
    }

    override val container by lazy { FabricLoader.getInstance().getModContainer("celeste").get() }
    override val storageDir by lazy { File(mc.runDirectory, "Celeste").apply { mkdirs() } }
    override val config by lazy { Config() }

    override fun onInitializeClient() {
        instance = this
        ModuleSystem.register()
        Listeners.register()
    }
}

interface ICelesteClient {
    val container: ModContainer
    val name: String get() = container.metadata.name
    val version: String get() = container.metadata.version.friendlyString

    val storageDir: File
    val config: Config

    @Suppress("unused")
    fun id(key: String) = Identifier("celeste", key)
}

@Suppress("unused")
object Client : ICelesteClient by CelesteClient.instance