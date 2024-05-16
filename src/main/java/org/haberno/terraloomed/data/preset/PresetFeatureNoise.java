package org.haberno.terraloomed.data.preset;


import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.noise.module.Noises;

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
