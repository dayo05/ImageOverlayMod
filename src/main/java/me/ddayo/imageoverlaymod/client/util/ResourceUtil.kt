package me.ddayo.imageoverlaymod.client.util

import net.minecraft.client.Minecraft
import java.io.File
import java.nio.ByteBuffer

class ResourceUtil {
    companion object {
        @JvmStatic
        external fun getImage(name: String): ByteBuffer

        @JvmStatic
        external fun getVideo(name: String): ByteBuffer

        @JvmStatic
        external fun nextFrame(): Boolean

        @JvmStatic
        external fun getMediaWidth(): Int

        @JvmStatic
        external fun getMediaHeight(): Int

        @JvmStatic
        external fun getFrameRate(): Double

        val baseDir: File
            get() = File(Minecraft.getInstance().gameDir, "resources")

        val cacheDir: File
            get() = File(Minecraft.getInstance().gameDir, ".cache")
    }
}