package me.ddayo.imageoverlaymod.command

import me.ddayo.imageoverlaymod.ImageOverlayMod
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class CommandEventHandler {
    @SubscribeEvent
    fun onRegisterCommand(event: RegisterCommandsEvent) {
        ImageOverlayMod.LOGGER.info("Handle command event")
        DebugCommand.register(event.dispatcher)
        CachingCommand.register(event.dispatcher)
    }
}