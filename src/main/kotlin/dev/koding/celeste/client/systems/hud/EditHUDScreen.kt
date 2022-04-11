package dev.koding.celeste.client.systems.hud

import dev.koding.celeste.client.systems.hud.component.HUDElementComponent
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.provideDelegate
import java.awt.Color
import kotlin.random.Random

object EditHUDScreen : WindowScreen(ElementaVersion.V1) {
    init {
        Inspector(window) childOf window
        HUDSystem.elements.forEach {
            HUDElementComponent(it, window) childOf window
        }
    }
}

class TestElement : HUDElement("test", "test", active = true) {
    private val container by UIBlock(Color.GREEN).constrain {
        width = ChildBasedSizeConstraint()
        height = ChildBasedSizeConstraint()
    } childOf this

    private val text by UIText("swag") childOf container

    override fun update() {
        text.setText("swag ${Random.nextInt(5)}")
        super.update()
    }
}