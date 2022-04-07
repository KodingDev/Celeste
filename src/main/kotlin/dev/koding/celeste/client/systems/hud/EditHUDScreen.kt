package dev.koding.celeste.client.systems.hud

import dev.koding.celeste.client.systems.hud.component.HUDElementComponent
import dev.koding.celeste.client.utils.mc
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.provideDelegate
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import java.awt.Color

class EditHUDScreen : WindowScreen(ElementaVersion.V1) {

    @Suppress("unused")
    val component by HUDElementComponent(TestElement(), window) childOf window

}

class TestElement : HUDElement("test", "test", active = true) {
    override fun update() {
        boundingBox.height = mc.textRenderer.fontHeight.toDouble()
        boundingBox.width = mc.textRenderer.getWidth("swag").toDouble()
    }

    override fun render(stack: MatrixStack) {
        Screen.fill(
            stack,
            boundingBox.x.toInt(),
            boundingBox.y.toInt(),
            (boundingBox.x + boundingBox.width).toInt(),
            (boundingBox.y + boundingBox.height).toInt(),
            Color(0, 0, 0, 100).rgb
        )
        mc.textRenderer.draw(stack, "swag", boundingBox.x.toFloat(), boundingBox.y.toFloat(), 0xFFFFFF)
    }
}