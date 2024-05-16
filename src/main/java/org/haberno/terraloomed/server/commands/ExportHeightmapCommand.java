package org.haberno.terraloomed.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ExportHeightmapCommand {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher, CommandRegistryAccess commandBuildContext) {
    	commandDispatcher.register(
    		CommandManager.literal("rtf").requires((stack) -> stack.hasPermissionLevel(2)).then(
    			CommandManager.literal("export").then(
    				CommandManager.literal("heightmap").then(
    					CommandManager.argument("x", IntegerArgumentType.integer()).then(
        					CommandManager.argument("z", IntegerArgumentType.integer()).then(
    	        				CommandManager.argument("size", IntegerArgumentType.integer()).executes((ctx) -> {
    	        					throw new UnsupportedOperationException("TODO");
    	        				})
    						)
    					)
        			)
    			)
    		)
    	);
    }
}