package dev.koding.celeste.client.systems.hud.component

import dev.koding.celeste.client.systems.hud.HUDElement
import dev.koding.celeste.client.utils.Mouse
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.*
import gg.essential.elementa.utils.Vector2f
import gg.essential.universal.UMatrixStack
import java.awt.Color
import kotlin.math.abs
import kotlin.math.min

private val SNAP_POINTS = arrayOf(
    Vector2f(0.5f, 0.5f), // center
    Vector2f(0f, 0.5f), // top middle
    Vector2f(0.5f, 0f), // middle left
    Vector2f(1f, 0.5f), // middle right
    Vector2f(0.5f, 1f), // bottom middle
    Vector2f(0f, 0f), // top left
    Vector2f(1f, 0f), // top right
    Vector2f(0f, 1f), // bottom left
    Vector2f(1f, 1f)  // bottom right
)

class HUDElementComponent(private val element: HUDElement, window: Window) : UIComponent() {

    private var dragging = false
    private var delta = Vector2f(0f, 0f)

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

            element.boundingBox.x += dx
            element.boundingBox.y += dy
            delta = Vector2f(Mouse.scaledX.toFloat(), Mouse.scaledY.toFloat())

            val elementSnapPoints = SNAP_POINTS.map {
                Vector2f(
                    (element.boundingBox.x + it.x * element.boundingBox.width).toFloat(),
                    (element.boundingBox.y + it.y * element.boundingBox.height).toFloat()
                )
            }

            // Screen snap coordinates for axis center
            val screenSnapPoints = SNAP_POINTS.map {
                Vector2f(
                    it.x * dev.koding.celeste.client.utils.Window.width,
                    it.y * dev.koding.celeste.client.utils.Window.height
                )
            }

            // TODO: Account for other elements
            // See: https://github.com/isXander/EvergreenHUD/blob/6821d50dee9bf55db6a78c0a69cdd362c45796e2/src/main/kotlin/dev/isxander/evergreenhud/ui/components/ElementComponent.kt#L109

            val snapThreshold = 5f
            var verticalSnap: Pair<Vector2f, Vector2f>? = null
            var horizontalSnap: Pair<Vector2f, Vector2f>? = null

            elementSnapPoints.forEach { point ->
                screenSnapPoints.forEach { screenPoint ->
                    if (horizontalSnap == null) {
                        if (abs(point.x - screenPoint.x) <= snapThreshold) {
                            horizontalSnap = point to screenPoint
                        }
                    }

                    if (verticalSnap == null) {
                        if (abs(point.y - screenPoint.y) <= snapThreshold) {
                            verticalSnap = point to screenPoint
                        }
                    }
                }
            }

            if (verticalSnap != null) {
                val origin =
                    verticalSnap!!.first.y - element.boundingBox.width - (element.boundingBox.y - element.boundingBox.width)
                element.boundingBox.y = verticalSnap!!.second.y - origin

                if (abs(element.boundingBox.y - Mouse.scaledY) > snapThreshold) {
                    element.boundingBox.y = Mouse.scaledY
                    verticalSnapGuide.reset()
                } else {
                    verticalSnapGuide.constrain {
                        x = min(verticalSnap!!.first.x, verticalSnap!!.second.x).pixels()
                        y = verticalSnap!!.second.y.pixels() - 1.pixels()
                        width = abs(verticalSnap!!.second.x - verticalSnap!!.first.x).pixels()
                        height = 2.pixels()
                    }
                }
            } else verticalSnapGuide.reset()

            if (horizontalSnap != null) {
                val origin =
                    horizontalSnap!!.first.x - element.boundingBox.height - (element.boundingBox.x - element.boundingBox.height)
                element.boundingBox.x = horizontalSnap!!.second.x - origin

                if (abs(element.boundingBox.x - Mouse.scaledX) > snapThreshold) {
                    element.boundingBox.x = Mouse.scaledX
                    horizontalSnapGuide.reset()
                } else {
                    horizontalSnapGuide.constrain {
                        x = horizontalSnap!!.second.x.pixels() - 1.pixels()
                        y = min(horizontalSnap!!.first.y, horizontalSnap!!.second.y).pixels()
                        width = 2.pixels()
                        height = abs(horizontalSnap!!.second.y - horizontalSnap!!.first.y).pixels()
                    }
                }
            } else horizontalSnapGuide.reset()

            element.boundingBox.calculateBorders()
        }
    }

    override fun draw(matrixStack: UMatrixStack) {
        beforeDrawCompat(matrixStack)
        recalculateHitbox()

        // TODO: Make hud elements components?
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

    private fun UIBlock.reset() = constrain {
        width = 0.pixels()
        height = 0.pixels()
    }

}