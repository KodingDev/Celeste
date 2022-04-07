package dev.koding.celeste.client.systems.hud.component

import dev.koding.celeste.client.systems.hud.HUDElement
import dev.koding.celeste.client.utils.Mouse
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.elementa.utils.Vector2f
import gg.essential.universal.UMatrixStack
import java.awt.Color

class HUDElementComponent(private val element: HUDElement, window: Window) : UIComponent() {

    private var dragging = false
    private var delta = Vector2f(0f, 0f)

    // TODO: Remove button

    private val verticalSnapGuide by UIBlock(Color.red) childOf window
    private val horizontalSnapGuide by UIBlock(Color.red) childOf window

    init {
        verticalSnapGuide.hide()
        horizontalSnapGuide.hide()

        onMouseClick {
            dragging = true
            delta = Vector2f(Mouse.scaledX.toFloat(), Mouse.scaledY.toFloat())
            verticalSnapGuide.unhide()
            horizontalSnapGuide.unhide()
        }

        onMouseRelease {
            dragging = false
            verticalSnapGuide.hide()
            horizontalSnapGuide.hide()
        }

        onMouseDrag { _, _, mouseButton ->
            if (!dragging || mouseButton != 0) return@onMouseDrag

            val dx = Mouse.scaledX - delta.x
            val dy = Mouse.scaledY - delta.y

            element.boundingBox.add(dx, dy)
            delta = Vector2f(Mouse.scaledX.toFloat(), Mouse.scaledY.toFloat())
        }
    }

    override fun draw(matrixStack: UMatrixStack) {
        beforeDrawCompat(matrixStack)
        recalculateHitbox()
        element.render(matrixStack.toMC())
        super.draw(matrixStack)
    }

    override fun onWindowResize() {
        super.onWindowResize()
        recalculateHitbox()
    }

    private fun recalculateHitbox() = constrain {
        element.update()
        width = element.boundingBox.width.pixels()
        height = element.boundingBox.height.pixels()
        x = element.boundingBox.x.pixels()
        y = element.boundingBox.y.pixels()
    }

}