package dev.koding.celeste.client.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.universal.UKeyboard

class KeyBindGUI(private var save: (Int) -> Unit) : WindowScreen(ElementaVersion.V1) {
    private val container by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint()
    } childOf window

    init {
        UIText("Press any key to rebind.").constrain {
            textScale = 2.pixels()
        } childOf container

        UIText("Press escape to cancel.").constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f)
        } childOf container
    }

    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
        if (keyCode != UKeyboard.KEY_ESCAPE) save(keyCode)
        close()
    }
}