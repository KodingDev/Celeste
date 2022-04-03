package dev.koding.celeste.client.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.koding.celeste.client.Client
import dev.koding.celeste.client.systems.module.ModuleSystem
import dev.koding.celeste.client.utils.div
import gg.essential.vigilance.Vigilant

class Config : Vigilant(Client.storageDir / "config.toml", guiTitle = "Celeste") {
    init {
        ModuleSystem.modules
            .filter { it.configBuilder != null }
            .groupBy { it.category }
            .forEach { (category, modules) ->
                category(category.title) {
                    modules.forEach {
                        subcategory("Module - ${it.name}", it.configBuilder!!)
                    }
                }
            }

        initialize()
    }
}

class ModMenuCompatibility : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory { Client.config.gui() }
}