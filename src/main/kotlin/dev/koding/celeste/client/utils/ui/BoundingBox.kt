package dev.koding.celeste.client.utils.ui

import dev.koding.celeste.client.utils.Window

/**
 * Field values for X and Y are percentages of the window size.
 */
class BoundingBox {

    var x: Double = 0.0
        set(value) {
            field = value / Window.width
        }
        get() = field * Window.width

    var y: Double = 0.0
        set(value) {
            field = value / Window.height
        }
        get() = field * Window.height

    var width: Double = 0.0
    var height: Double = 0.0

    fun calculateBorders() {
        if (this.x < 0) this.x = 0.0
        else if (this.x + width > Window.scaledWidth) this.x = Window.scaledWidth - width

        if (this.y < 0) this.y = 0.0
        else if (this.y + height > Window.scaledHeight) this.y = Window.scaledHeight - height
    }

    fun contains(x: Double, y: Double) = x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height

}