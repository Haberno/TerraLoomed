package org.haberno.terraloomed.data.preset;

import java.util.OptionalLong;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.data.preset.settings.WorldSettings;

public final class PresetDimensionTypes {
	
	public static void bootstrap(Preset preset, Registerable<DimensionType> ctx) {
		WorldSettings worldSettings = preset.world();
		WorldSettings.Properties properties = worldSettings.properties;
		int worldHeight = properties.worldHeight;
		int worldDepth = properties.worldDepth;
		int totalHeight = worldDepth + worldHeight;
		
        ctx.register(DimensionTypes.OVERWORLD, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, false, -worldDepth, totalHeight, totalHeight, BlockTags.INFINIBURN_OVERWORLD, DimensionTypes.OVERWORLD_ID, 0.0f, new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)));
	}
}
