package org.haberno.terraloomed.registries;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.registry.Registries;
import org.haberno.terraloomed.server.commands.TerrainArgument;

public class RTFArgumentTypeInfos {

	public static void bootstrap() {
        ArgumentTypes.register(Registries.COMMAND_ARGUMENT_TYPE, "reterraforged:terrain", TerrainArgument.class, new TerrainArgument.Info());
	}
}
