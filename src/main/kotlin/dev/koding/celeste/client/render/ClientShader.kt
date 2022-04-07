package dev.koding.celeste.client.render

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.koding.celeste.client.Client
import dev.koding.celeste.client.utils.mc
import net.minecraft.util.math.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20C

class ClientShader(
    vertex: String,
    fragment: String
) {

    companion object {
        var current: ClientShader? = null
            private set

        val posColor = ClientShader("pos_color.vert", "pos_color.frag")
        val posColorTex = ClientShader("pos_color_tex.vert", "pos_color_tex.frag")
    }

    private val locationCache = hashMapOf<String, Int>()

    private val program = run {
        val vert = GlStateManager.glCreateShader(GL20C.GL_VERTEX_SHADER)
        GlStateManager.glShaderSource(vert, listOf(resource(vertex)))
        OpenGL.compileShader(vert)

        val frag = GlStateManager.glCreateShader(GL20C.GL_FRAGMENT_SHADER)
        GlStateManager.glShaderSource(frag, listOf(resource(fragment)))
        OpenGL.compileShader(frag)

        val program = GlStateManager.glCreateProgram()
        OpenGL.linkProgram(program, vert, frag)

        GlStateManager.glDeleteShader(vert)
        GlStateManager.glDeleteShader(frag)

        program
    }

    fun bind() {
        current = this
        GlStateManager._glUseProgram(program)
    }

    operator fun set(name: String, value: Any) {
        val location = getLocation(name)
        when (value) {
            is Int -> GlStateManager._glUniform1i(location, value)
            is Boolean -> GlStateManager._glUniform1i(location, if (value) GL20C.GL_TRUE else GL20C.GL_FALSE)
            is Float -> GL20C.glUniform1f(location, value)
            is Matrix4f -> {
                val buf = BufferUtils.createFloatBuffer(4 * 4)
                value.writeColumnMajor(buf)
                GlStateManager._glUniformMatrix4(location, false, buf)
            }
            else -> throw IllegalArgumentException("Unsupported type: $value")
        }
    }

    fun setDefaults() {
        this["u_Proj"] = RenderSystem.getProjectionMatrix()
        this["u_ModelView"] = RenderSystem.getModelViewStack().peek().positionMatrix
    }

    private fun resource(name: String) =
        mc.resourceManager.getResource(Client.id("shaders/$name")).inputStream.readBytes().decodeToString()

    private fun getLocation(name: String) =
        locationCache.getOrPut(name) { GlStateManager._glGetUniformLocation(program, name) }

}