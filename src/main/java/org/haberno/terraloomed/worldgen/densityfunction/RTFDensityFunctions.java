package org.haberno.terraloomed.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.haberno.terraloomed.platform.RegistryUtil;
import org.haberno.terraloomed.worldgen.cell.CellField;
import org.haberno.terraloomed.worldgen.noise.module.Noise;

public class RTFDensityFunctions {

	public static void bootstrap() {
		register("noise_sampler", NoiseSampler.Marker.CODEC);
		register("cell", CellSampler.Marker.CODEC);
		register("clamp_to_nearest_unit", ClampToNearestUnit.CODEC);
		register("linear_spline", LinearSplineFunction.CODEC);
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
