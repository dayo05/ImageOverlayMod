package me.ddayo.imageoverlaymod.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import me.ddayo.imageoverlaymod.ImageOverlayMod
import me.ddayo.imageoverlaymod.client.util.ResourceUtil
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import org.lwjgl.opengl.GL11
import java.awt.image.BufferedImage
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO

class ImageGui(private val image: String): Screen(StringTextComponent("")) {
    private var texture = -1

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (texture == -1) {
            texture = GL11.glGenTextures()
            ImageOverlayMod.LOGGER.info("asdfasdf")
/*
            val file: BufferedImage = ImageIO.read(File(ResourceUtil.baseDir, image))
            val buf locateDirect(file.width * file.height * 4)

            for (pix in file.getRGB(0, 0, file.width, file.height, null, 0, file.width).asSequence().map { it.toUInt() and 0xFFFFFFFFu }) {
                buf.put((pix and 255u).toByte()) //R
                buf.put(((pix shr 8) and 255u).toByte()) //G
                buf.put(((pix shr 16) and 255u).toByte()) //B
                buf.put((pix shr 24).toByte()) //A
            }
            buf.flip()
 */
            val buf = ResourceUtil.getImage(File(ResourceUtil.baseDir, image).absolutePath)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP)
            //GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, file.width, file.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf.asIntBuffer())
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ResourceUtil.getMediaWidth(), ResourceUtil.getMediaHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf.asIntBuffer())
            ImageOverlayMod.LOGGER.info("asdfasdf")
            return
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
        GL11.glPushMatrix()
        run {
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor3d(1.0, 1.0, 1.0)

            GL11.glTexCoord2d(1.0, 0.0)
            GL11.glVertex2i(width, 0)
            GL11.glTexCoord2d(0.0, 0.0)
            GL11.glVertex2i(0, 0)
            GL11.glTexCoord2d(0.0, 1.0)
            GL11.glVertex2i(0, height)
            GL11.glTexCoord2d(1.0, 1.0)
            GL11.glVertex2i(width, height)
        }
        GL11.glPopMatrix()
        super.render(matrixStack, mouseX, mouseY, partialTicks)
    }
}