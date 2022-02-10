package me.ddayo.imageoverlaymod.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.imageoverlaymod.client.util.ResourceUtil
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import org.lwjgl.opengl.GL11
import java.io.File
import java.time.LocalDateTime

class VideoGui(private val video: String): Screen(StringTextComponent("")) {

    private val buf = ResourceUtil.getVideo(File(ResourceUtil.baseDir, video).absolutePath)
    private var texture = -1
    private val frameRate = ResourceUtil.getFrameRate()
    private var nextRenderTime = LocalDateTime.now()

    private val vWidth = ResourceUtil.getMediaWidth()
    private val vHeight = ResourceUtil.getMediaHeight()
    private var finalize = false
    private var initialized = false

    init {
        initialized = true
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        if(!initialized) return

        if (texture == -1) {
            //Required to initialize at render thread
            texture = GL11.glGenTextures()

            ResourceUtil.nextFrame()
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP)
            return
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
        if (LocalDateTime.now().isAfter(nextRenderTime)) {

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, vWidth, vHeight, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buf.asIntBuffer())
            ResourceUtil.nextFrame()

            nextRenderTime = LocalDateTime.now().plusNanos((1000000000 / frameRate).toLong())
        }
        GL11.glFlush()
        GL11.glFinish()
        GL11.glPushMatrix()
        run {
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glBegin(GL11.GL_QUADS)
            GL11.glColor3d(1.0, 1.0, 1.0)

            GL11.glTexCoord2d(1.0, 0.0)
            GL11.glVertex2i(width, 0)
            GL11.glTexCoord2d(0.0, 0.0)
            GL11.glVertex2i(0, 0)
            GL11.glTexCoord2d(0.0, 1.0)
            GL11.glVertex2i(0, height)
            GL11.glTexCoord2d(1.0, 1.0)
            GL11.glVertex2i(width, height)
            GL11.glEnd()
        }
        GL11.glPopMatrix()
        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }

    override fun onClose() {
        finalize = true
        super.onClose()
    }
}