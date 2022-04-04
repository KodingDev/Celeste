package dev.koding.celeste.client.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.koding.celeste.client.Client
import dev.koding.celeste.client.gui.KeyBindGUI
import dev.koding.celeste.client.systems.module.ModuleSystem
import dev.koding.celeste.client.utils.div
import dev.koding.celeste.client.utils.mc
import gg.essential.vigilance.Vigilant
import kotlin.reflect.KMutableProperty0

class Config : Vigilant(Client.storageDir / "config.toml", guiTitle = "Celeste") {
    var commandPrefix = "."

    init {
        category("Celeste") {
            subcategory("Commands") {
                text(::commandPrefix, "Command Prefix", "Prefix for custom chat commands")
            }
        }

        ModuleSystem.modules
            .groupBy { it.category }
            .forEach { (category, modules) ->
                category(category.title) {
                    modules.forEach {
                        subcategory("Module - ${it.name}") {
                            key(it.key::key)
                            it.configBuilder?.invoke(this)
                        }
                    }
                }
            }

        initialize()
    }

    private fun CategoryPropertyBuilder.key(field: KMutableProperty0<Int>, name: String = "Key Binding") {
        button(name, description = "Press a key to bind", triggerActionOnInitialization = false) {
            mc.setScreen(KeyBindGUI {
                field.set(it)
                markDirty()
                writeData()
            })
        }
        number(field, name = name, min = Int.MIN_VALUE, max = Int.MAX_VALUE, hidden = true)
    }
}

class ModMenuCompatibility : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory { Client.config.gui() }
}