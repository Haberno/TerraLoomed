package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;

public class PresetFeatureNoise {
	public static final RegistryKey<Noise> MEADOW_TREES = createKey("meadow_trees");
	
	public static void bootstrap(Preset preset, Registerable<Noise> ctx) {
		ctx.register(MEADOW_TREES, createMeadowTrees());
	}
	
	private static Noise createMeadowTrees() {
		return Noises.simplex(0, 75, 2);
	}
	
	public static RegistryKey<Noise> createKey(String name) {
        return PresetNoiseData.createKey("features/" + name);
	}
}
