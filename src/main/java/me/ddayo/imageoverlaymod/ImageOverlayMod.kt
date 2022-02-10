package me.ddayo.imageoverlaymod

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.common.MinecraftForge
import me.ddayo.imageoverlaymod.ImageOverlayMod.Companion.MOD_ID
import me.ddayo.imageoverlaymod.client.util.ResourceUtil
import me.ddayo.imageoverlaymod.command.CommandEventHandler
import net.minecraft.client.Minecraft
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import java.io.File

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
class ImageOverlayMod {
    companion object {
        const val MOD_ID = "image_overlay_mod"

        // Directly reference a log4j logger.
        public val LOGGER = LogManager.getLogger()

        init {
            System.loadLibrary("mediaIO")
        }
    }

    @SubscribeEvent
    fun setup(event: FMLCommonSetupEvent) {
        LOGGER.info("setup called")
        MinecraftForge.EVENT_BUS.register(CommandEventHandler())
    }

    @SubscribeEvent
    fun doClientStuff(event: FMLClientSetupEvent) {
        LOGGER.info("Client stuff called")
        if(!ResourceUtil.baseDir.exists())
            ResourceUtil.baseDir.mkdir()
        if(!ResourceUtil.cacheDir.exists())
            ResourceUtil.cacheDir.mkdir()
    }

    init {
        FMLJavaModLoadingContext.get().modEventBus.register(this)
    }
}