package dev.koding.celeste.client.systems.hud

import dev.koding.celeste.client.render.Render2D
import dev.koding.celeste.client.systems.hud.component.HUDElementComponent
import dev.koding.celeste.client.utils.mc
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.provideDelegate
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
        Render2D.color.use {
            quad(
                boundingBox.x.toFloat(),
                boundingBox.y.toFloat(),
                boundingBox.width.toFloat(),
                boundingBox.height.toFloat(),
                Color(0, 0, 0, 100)
            )
        }

        mc.textRenderer.draw(stack, "swag", boundingBox.x.toFloat(), boundingBox.y.toFloat(), 0xFFFFFF)
    }
}