package dev.koding.celeste.client.render

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11C
import org.lwjgl.opengl.GL20C

object OpenGL {

    private val trackedStates = listOf(
        GlStateManager.BLEND.capState,
        GlStateManager.DEPTH.capState,
        GlStateManager.CULL.capState,
        GlStateManager.SCISSOR.capState,
    )

    private val trackedStateValues = mutableMapOf<Int, Boolean>()

    fun pushState() = trackedStates.forEach { trackedStateValues[it.cap] = it.state }

    fun popState() {
        trackedStates.forEach { it.setState(trackedStateValues[it.cap] ?: false) }
        disableLineSmooth()
    }

    fun enableLineSmooth() {
        GL11C.glEnable(GL11C.GL_LINE_SMOOTH)
        GL11C.glLineWidth(1f)
    }

    private fun disableLineSmooth() = GL11C.glDisable(GL11C.GL_LINE_SMOOTH)

    fun compileShader(id: Int) {
        GlStateManager.glCompileShader(id)
        if (GlStateManager.glGetShaderi(id, GL20C.GL_COMPILE_STATUS) == GL11C.GL_FALSE) {
            throw RuntimeException("Shader compilation failed: ${GlStateManager.glGetShaderInfoLog(id, 512)}")
        }
    }

    fun linkProgram(program: Int, vertexShader: Int, fragmentShader: Int) {
        GlStateManager.glAttachShader(program, vertexShader)
        GlStateManager.glAttachShader(program, fragmentShader)
        GlStateManager.glLinkProgram(program)

        if (GlStateManager.glGetProgrami(program, GL20C.GL_LINK_STATUS) == GL11C.GL_FALSE) {
            throw RuntimeException("Program linking failed: ${GlStateManager.glGetProgramInfoLog(program, 512)}")
        }
    }

}