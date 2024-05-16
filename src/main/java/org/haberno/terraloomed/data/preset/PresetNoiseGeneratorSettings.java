package org.haberno.terraloomed.data.preset;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.haberno.terraloomed.data.preset.settings.CaveSettings;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.data.preset.settings.WorldSettings;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.worldgen.noise.module.Noise;

public class PresetNoiseGeneratorSettings {
	
	public static void bootstrap(Preset preset, Registerable<ChunkGeneratorSettings> ctx) {
		RegistryEntryLookup<DensityFunction> densityFunctions = ctx.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION);
		RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParams = ctx.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS);
		RegistryEntryLookup<Noise> noises = ctx.getRegistryLookup(RTFRegistries.NOISE);
		
		WorldSettings worldSettings = preset.world();
		WorldSettings.Properties properties = worldSettings.properties;
		int worldHeight = properties.worldHeight;
		int worldDepth = properties.worldDepth;
//    	Levels levels = new Levels(properties.terrainScaler(), properties.seaLevel);
    	
		CaveSettings caveSettings = preset.caves();

		ctx.register(ChunkGeneratorSettings.OVERWORLD, new ChunkGeneratorSettings(
			GenerationShapeConfig.create(-worldDepth, worldDepth + worldHeight, 1, 2), 
			Blocks.STONE.getDefaultState(), Blocks.WATER.getDefaultState(), 
			PresetNoiseRouterData.overworld(preset, densityFunctions, noiseParams, noises),
			PresetSurfaceRuleData.overworld(preset, noises),
			properties.spawnType.getParameterPoints(), 
			properties.seaLevel, 
			false, 
			true, 
			caveSettings.largeOreVeins, 
			false
		));
    }
}
