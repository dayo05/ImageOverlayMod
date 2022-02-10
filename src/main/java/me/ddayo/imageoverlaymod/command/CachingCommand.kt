package me.ddayo.imageoverlaymod.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import me.ddayo.imageoverlaymod.client.util.ResourceUtil
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands

class CachingCommand {
    companion object {
        public fun register(dispatcher: CommandDispatcher<CommandSource>) {
            dispatcher.register(Commands.literal("cache")
                    .requires { it.hasPermissionLevel(2) }
                    .then(Commands.argument("video", StringArgumentType.string())
                            .executes { command ->
                                //ResourceUtil.generateCache(command.getArgument("video", String::class.java))
                                return@executes 1
                            }))
        }
    }
}