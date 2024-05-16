package org.haberno.terraloomed.data.preset;


import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import org.haberno.terraloomed.data.preset.settings.ClimateSettings;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.data.preset.settings.WorldSettings;
import org.haberno.terraloomed.worldgen.noise.module.Noise;

public class PresetClimateNoise {
	public static final RegistryKey<Noise> BIOME_EDGE_SHAPE = createKey("biome_edge_shape");
	
	public static void bootstrap(Preset preset, Registerable<Noise> ctx) {
		WorldSettings worldSettings = preset.world();
		WorldSettings.Properties properties = worldSettings.properties;
		
		ClimateSettings climateSettings = preset.climate();
		ClimateSettings.BiomeNoise biomeEdgeShape = climateSettings.biomeEdgeShape;
		
		ctx.register(BIOME_EDGE_SHAPE, biomeEdgeShape.build(0));
	}

	private static RegistryKey<Noise> createKey(String name) {
		return PresetNoiseData.createKey("climate/" + name);
	}
}
