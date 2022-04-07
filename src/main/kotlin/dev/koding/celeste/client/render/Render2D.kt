package dev.koding.celeste.client.render

import java.awt.Color

class Render2D(textured: Boolean) {

    @Suppress("unused")
    companion object {
        val color = Render2D(false)
        val texture = Render2D(true)
    }

    private val triangles = ShadedMesh(
        Mesh.DrawMode.TRIANGLES,
        if (textured) ClientShader.posColorTex else ClientShader.posColor,
        *(if (textured) arrayOf(Mesh.AttributeType.VEC2, Mesh.AttributeType.VEC2, Mesh.AttributeType.COLOR)
        else arrayOf(Mesh.AttributeType.VEC2, Mesh.AttributeType.COLOR))
    )

    private val lines =
        ShadedMesh(Mesh.DrawMode.LINES, ClientShader.posColor, Mesh.AttributeType.VEC2, Mesh.AttributeType.COLOR)

    /**
     * Delegates to each mesh
     */
    private fun begin() {
        triangles.begin()
        lines.begin()
    }

    private fun render() {
        triangles.render()
        lines.render()
    }

    fun use(block: Render2D.() -> Unit) {
        begin()
        this.block()
        render()
    }

    /**
     * Quad rendering
     */
    fun quad(x: Float, y: Float, width: Float, height: Float, color: Color) =
        triangles.quad(
            triangles.vec2(x, y).color(color).next(),
            triangles.vec2(x, y + height).color(color).next(),
            triangles.vec2(x + width, y + height).color(color).next(),
            triangles.vec2(x + width, y).color(color).next()
        )

}