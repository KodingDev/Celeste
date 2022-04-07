package dev.koding.celeste.client.render

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL32C
import org.lwjgl.system.MemoryUtil
import java.awt.Color
import java.nio.ByteBuffer

open class Mesh(
    private val mode: DrawMode,
    vararg attributes: AttributeType
) {

    @Suppress("unused")
    enum class AttributeType(val size: Int) {
        FLOAT(1),
        VEC2(2),
        VEC3(3),
        COLOR(4)
    }

    enum class DrawMode(val indices: Int, val mode: Int) {
        LINES(2, GL32C.GL_LINES),
        TRIANGLES(3, GL32C.GL_TRIANGLES),
    }

    private val stride = attributes.sumOf { it.size } * 4
    private val vertexSize = stride * mode.indices

    private val vertices = PointedMemory(vertexSize * 256, 4) {
        var size = it * 2
        if (size % vertexSize != 0) size += size % vertexSize
        size
    }

    private val indices = PointedMemory(mode.indices * 512, 4) {
        var size = it * 2
        if (size % mode.indices != 0) size += size % (mode.indices * 4)
        size
    }

    private val vao = GlStateManager._glGenVertexArrays()
    private val vbo = GlStateManager._glGenBuffers()
    private val ibo = GlStateManager._glGenBuffers()

    private var building = false
    private var vertexI = 0

    init {
        GlStateManager._glBindVertexArray(vao)
        GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, vbo)
        GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, ibo)

        var offset = 0L
        attributes.forEachIndexed { index, type ->
            GlStateManager._enableVertexAttribArray(index)
            GlStateManager._vertexAttribPointer(index, type.size, GL32C.GL_FLOAT, false, stride, offset)
            offset += type.size * 4
        }

        GlStateManager._glBindVertexArray(0)
        GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, 0)
        GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    /**
     * Mesh building
     */
    fun begin() {
        if (building) throw IllegalStateException("Already building")
        building = true

        // TODO: 3D support
        vertexI = 0
        vertices.index = 0
        indices.index = 0
    }

    private fun end() {
        if (!building) throw IllegalStateException("Not building")
        building = false

        if (indices.index > 0) {
            GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, vbo)
            GlStateManager._glBufferData(
                GL32C.GL_ARRAY_BUFFER,
                vertices.buffer.limit(vertices.index * 4),
                GL32C.GL_DYNAMIC_DRAW
            )
            GlStateManager._glBindBuffer(GL32C.GL_ARRAY_BUFFER, 0)

            GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, ibo)
            GlStateManager._glBufferData(
                GL32C.GL_ELEMENT_ARRAY_BUFFER,
                indices.buffer.limit(indices.index * 4),
                GL32C.GL_DYNAMIC_DRAW
            )
            GlStateManager._glBindBuffer(GL32C.GL_ELEMENT_ARRAY_BUFFER, 0)
        }
    }

    /**
     * Adding elements
     */
    fun vec2(x: Float, y: Float): Mesh {
        if (!building) throw IllegalStateException("Not building")
        MemoryUtil.memPutFloat(vertices.next(), x)
        MemoryUtil.memPutFloat(vertices.next(), y)
        return this
    }

    fun color(color: Color): Mesh {
        if (!building) throw IllegalStateException("Not building")
        MemoryUtil.memPutFloat(vertices.next(), color.red / 255f)
        MemoryUtil.memPutFloat(vertices.next(), color.green / 255f)
        MemoryUtil.memPutFloat(vertices.next(), color.blue / 255f)
        MemoryUtil.memPutFloat(vertices.next(), color.alpha / 255f)
        return this
    }

    /**
     * Basic shapes
     */
    fun quad(p1: Int, p2: Int, p3: Int, p4: Int) {
        MemoryUtil.memPutInt(indices.next(), p1)
        MemoryUtil.memPutInt(indices.next(), p2)
        MemoryUtil.memPutInt(indices.next(), p3)

        MemoryUtil.memPutInt(indices.next(), p3)
        MemoryUtil.memPutInt(indices.next(), p4)
        MemoryUtil.memPutInt(indices.next(), p1)

        grow()
    }

    /**
     * Buffers
     */
    fun next(): Int {
        if (!building) throw IllegalStateException("Not building")
        return vertexI++
    }

    private fun grow() {
        if (!building) throw IllegalStateException("Not building")
        if ((vertexI + 1) * vertexSize > vertices.buffer.capacity()) vertices.grow()
        if (indices.index * 4 > indices.buffer.capacity()) indices.grow()
    }

    /**
     * Rendering
     */
    private fun startRender() {
        OpenGL.pushState()

        GlStateManager._disableDepthTest()
        GlStateManager._enableBlend()
        GlStateManager._disableCull()
        OpenGL.enableLineSmooth()

        // TODO: 3D support
    }

    private fun endRender() {
        // TODO: 3D support
        OpenGL.popState()
    }

    fun render() {
        if (building) end()
        if (indices.index == 0) return

        startRender()
        preRender()

        ClientShader.current?.setDefaults()
        GlStateManager._glBindVertexArray(vao)
        GlStateManager._drawElements(mode.mode, indices.index, GL32C.GL_UNSIGNED_INT, 0)
        GlStateManager._glBindVertexArray(0)
        endRender()
    }

    open fun preRender() {}
}

class ShadedMesh(
    mode: DrawMode,
    private val shader: ClientShader,
    vararg attributes: AttributeType
) : Mesh(mode, *attributes) {
    override fun preRender() = shader.bind()
}

class PointedMemory(
    size: Int,
    private val componentSize: Int,
    private val increaseSize: (capacity: Int) -> Int = { it * 2 }
) {
    var buffer: ByteBuffer = BufferUtils.createByteBuffer(size * componentSize)
        private set

    private var pointer = MemoryUtil.memAddress0(buffer)
    private val trackedPointer get() = pointer + index * componentSize

    var index = 0

    fun next(components: Int = 1) = trackedPointer.also { index += components }

    fun grow() {
        val buf = BufferUtils.createByteBuffer(increaseSize(buffer.capacity()))
        MemoryUtil.memCopy(pointer, MemoryUtil.memAddress0(buf), buffer.capacity().toLong())

        buffer = buf
        pointer = MemoryUtil.memAddress0(buffer)
    }
}