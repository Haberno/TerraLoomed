package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;

public class PresetTerrainNoise {
	public static final RegistryKey<Noise> MOUNTAIN_CHAIN_ALPHA = createKey("mountain_chain_alpha");
	public static final RegistryKey<Noise> RIVER_ALPHA = createKey("river_alpha");
	public static final RegistryKey<Noise> LAKE_ALPHA = createKey("lake_alpha");
	public static final RegistryKey<Noise> SWAMP_ALPHA = createKey("swamp_alpha");
	
	public static final RegistryKey<Noise> EROSION = createKey("erosion");
	public static final RegistryKey<Noise> RIDGES = createKey("ridges");
	public static final RegistryKey<Noise> RIDGES_FOLDED = createKey("ridges_folded");
	public static final RegistryKey<Noise> OFFSET = createKey("offset");
	
	public static void bootstrap(Preset preset, Registerable<Noise> ctx) {
	}

	protected static RegistryKey<Noise> createKey(String name) {
		return PresetNoiseData.createKey("terrain/" + name);
	}
}
