package dev.koding.celeste.client.systems.module

import dev.koding.celeste.client.event.Listener
import dev.koding.celeste.client.utils.Chat
import dev.koding.celeste.client.utils.KeyBind
import gg.essential.vigilance.Vigilant
import net.minecraft.item.Item
import net.minecraft.item.Items

@Suppress("unused")
abstract class Module(
    val category: ModuleCategory,
    val icon: Item,
    val name: String,
    val description: String,
    val cheat: Boolean = true,
    var key: KeyBind? = null
) : Listener {
    private var active = false
    var configBuilder: (Vigilant.CategoryPropertyBuilder.() -> Unit)? = null
        private set

    open fun onEnable() {}
    open fun onDisable() {}

    protected fun config(builder: Vigilant.CategoryPropertyBuilder.() -> Unit) {
        if (configBuilder != null) {
            throw IllegalStateException("Config builder already set")
        }

        configBuilder = builder
    }

    fun toggle() = setActive(!active)

    private fun setActive(active: Boolean) {
        if (active == this.active) return
        this.active = active

        if (active) {
            onEnable()
            register()
        } else {
            onDisable()
            unregister()
        }

        Chat.info("${if (active) "Enabled" else "Disabled"} module $name", id = "module-$name".hashCode())
    }
}

@Suppress("unused")
enum class ModuleCategory(val title: String, val icon: Item) {
    COMBAT("Combat", Items.DIAMOND_SWORD),
    MOVEMENT("Movement", Items.DIAMOND_BOOTS),
    RENDER("Render", Items.TINTED_GLASS),
    WORLD("World", Items.GRASS_BLOCK),
    MISC("Misc", Items.NETHER_STAR)
}