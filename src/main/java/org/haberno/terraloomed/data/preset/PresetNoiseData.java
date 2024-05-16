package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;

public class PresetNoiseData {

	public static void bootstrap(Preset preset, Registerable<Noise> ctx) {
		PresetTerrainNoise.bootstrap(preset, ctx);
		PresetClimateNoise.bootstrap(preset, ctx);
		PresetSurfaceNoise.bootstrap(preset, ctx);
		PresetStrataNoise.bootstrap(preset, ctx);
		PresetFeatureNoise.bootstrap(preset, ctx);
	}
	
	public static Noise getNoise(RegistryEntryLookup<Noise> noiseLookup, RegistryKey<Noise> key) {
		return new Noises.HolderHolder(noiseLookup.getOrThrow(key));
	}
	
	public static Noise registerAndWrap(Registerable<Noise> ctx, RegistryKey<Noise> key, Noise noise) {
		return new Noises.HolderHolder(ctx.register(key, noise));
	}
	
	public static RegistryKey<Noise> createKey(String name) {
        return RegistryKey.of(RTFRegistries.NOISE, RTFCommon.location(name));
	}
}
