package org.haberno.terraloomed.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import raccoonman.reterraforged.platform.RegistryUtil;
import raccoonman.reterraforged.world.worldgen.cell.CellField;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;	

public class RTFDensityFunctions {

	public static void bootstrap() {
		register("noise_sampler", NoiseSampler.Marker.REGISTRY_ENTRY_CODEC);
		register("cell", CellSampler.Marker.REGISTRY_ENTRY_CODEC);
		register("clamp_to_nearest_unit", ClampToNearestUnit.REGISTRY_ENTRY_CODEC);
		register("linear_spline", LinearSplineFunction.REGISTRY_ENTRY_CODEC);
	}
	
	public static NoiseSampler.Marker noise(RegistryEntry<Noise> noise) {
		return new NoiseSampler.Marker(noise);
	}
	
	public static CellSampler.Marker cell(CellField field) {
		return new CellSampler.Marker(field);
	}
	
	public static ClampToNearestUnit clampToNearestUnit(DensityFunction function, int resolution) {
		return new ClampToNearestUnit(function, resolution);
	}
	
	private static void register(String name, Codec<? extends DensityFunction> type) {
		RegistryUtil.register(Registries.DENSITY_FUNCTION_TYPE, name, type);
	}
}
