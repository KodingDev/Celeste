package dev.koding.celeste.client.systems.module

import dev.koding.celeste.client.event.Listener
import dev.koding.celeste.client.utils.KeyBind
import net.minecraft.item.Item
import net.minecraft.item.Items

@Suppress("unused")
abstract class Module(
    private val category: ModuleCategory,
    private val icon: Item,
    private val name: String,
    private val description: String,
    private val cheat: Boolean = true,
    var key: KeyBind? = null
) : Listener {
    private var active = false

    open fun onEnable() {}
    open fun onDisable() {}

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