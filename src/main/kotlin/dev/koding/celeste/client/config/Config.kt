package dev.koding.celeste.client.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.koding.celeste.client.Client
import dev.koding.celeste.client.utils.div
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType

class Config : Vigilant(Client.storageDir / "config.toml", guiTitle = "Celeste") {

    @Suppress("unused")
    @Property(
        type = PropertyType.SLIDER,
        name = "my balls!",
        category = "Celeste",
        max = 50,
        min = -1 // for systemless
    )
    var myBalls: Int = 0

    init {
        initialize()
    }

}

class ModMenuCompatibility : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory { Client.config.gui() }
}