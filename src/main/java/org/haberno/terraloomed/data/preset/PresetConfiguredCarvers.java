package org.haberno.terraloomed.data.preset;

import net.minecraft.block.Block;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import org.haberno.terraloomed.data.preset.settings.CaveSettings;
import org.haberno.terraloomed.data.preset.settings.Preset;

public class PresetConfiguredCarvers {

	//TODO make lava level configurable
	public static void bootstrap(Preset preset, Registerable<ConfiguredCarver<?>> ctx) {
		CaveSettings caveSettings = preset.caves();
        RegistryEntryLookup<Block> blocks = ctx.getRegistryLookup(RegistryKeys.BLOCK);
        
//        ctx.register(Carvers.CAVE, WorldCarver.CAVE.configured(new CaveCarverConfiguration(caveSettings.caveCarverChance, modifiedCaveY(caveSettings), modifiedCaveYScale(caveSettings), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()), blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), modifiedCaveHorizontalRadiusMultiplier(caveSettings), modifiedCaveVerticalRadiusMultiplier(caveSettings), modifiedCaveFloorLevel(caveSettings))));
//        ctx.register(Carvers.CAVE_EXTRA_UNDERGROUND, WorldCarver.CAVE.configured(new CaveCarverConfiguration(modifiedDeepCaveChance(caveSettings), UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(47)), UniformFloat.of(0.1F, 0.9F), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.OAK_BUTTON.defaultBlockState()), blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), UniformFloat.of(0.7F, 1.4F), UniformFloat.of(0.8F, 1.3F), UniformFloat.of(-1.0F, -0.4F))));
//        ctx.register(Carvers.CANYON, WorldCarver.CANYON.configured(new CanyonCarverConfiguration(caveSettings.ravineCarverChance, modifiedRavineY(caveSettings), ConstantFloat.of(3.0F), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()), blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), modifiedRavineYScale(caveSettings), new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75F, 1.0F), TrapezoidFloat.of(0.0F, 6.0F, 2.0F), 3, UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F))));
	}
}
