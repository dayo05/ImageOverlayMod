package me.ddayo.imageoverlaymod.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import me.ddayo.imageoverlaymod.client.gui.VideoGui
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class DebugCommand(val s: String) {
    companion object {
        public fun register(dispatcher: CommandDispatcher<CommandSource>) {
            dispatcher.register(Commands.literal("asdf")
                    .requires {
                        it.hasPermissionLevel(2)
                    }
                    .then(Commands.argument("message", StringArgumentType.string())
                            .executes { commandContext ->
                                if(Minecraft.getInstance().isSingleplayer) {
                                    MinecraftForge.EVENT_BUS.register(DebugCommand(commandContext.getArgument("message", String::class.java)))
                                }
                                return@executes 0
                            })
            )
        }
    }

    @SubscribeEvent
    fun e(event: TickEvent) {
        Minecraft.getInstance().displayGuiScreen(VideoGui(s))
        MinecraftForge.EVENT_BUS.unregister(this)
    }
}