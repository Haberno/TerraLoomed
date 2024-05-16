package org.haberno.terraloomed.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.BiConsumer;

public class RTFCommands {

	public static void bootstrap() {
		register(LocateTerrainCommand::register);
		register(ExportHeightmapCommand::register);
	}
	
	public static void register(BiConsumer<CommandDispatcher<ServerCommandSource>, CommandRegistryAccess> register) {
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> register.accept(dispatcher, buildContext));
	}
}
