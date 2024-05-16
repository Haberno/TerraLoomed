package org.haberno.terraloomed.server.commands;

import java.util.function.BiConsumer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.CommandDispatcher;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class RTFCommands {

	public static void bootstrap() {
		register(LocateTerrainCommand::register);
		register(ExportHeightmapCommand::register);
	}
	
	@ExpectPlatform
	public static void register(BiConsumer<CommandDispatcher<ServerCommandSource>, CommandRegistryAccess> register) {
		throw new UnsupportedOperationException();
	}
}
