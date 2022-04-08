package dev.koding.celeste.client.utils.ui

import gg.essential.universal.UResolution

/**
 * Field values for X and Y are percentages of the window size.
 */
class BoundingBox {

    var xPercent = 0.0
    var yPercent = 0.0

    var x: Double
        set(value) {
            xPercent = value / UResolution.scaledWidth
        }
        get() = xPercent * UResolution.scaledWidth

    var y: Double
        set(value) {
            yPercent = value / UResolution.scaledHeight
        }
        get() = yPercent * UResolution.scaledHeight

    var width: Double = 0.0
    var height: Double = 0.0

    fun calculateBorders() {
        if (this.x < 0) this.x = 0.0
        else if (this.x + width > UResolution.scaledWidth) this.x = UResolution.scaledWidth - width

        if (this.y < 0) this.y = 0.0
        else if (this.y + height > UResolution.scaledHeight) this.y = UResolution.scaledHeight - height
    }

    fun contains(x: Double, y: Double) = x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height

}