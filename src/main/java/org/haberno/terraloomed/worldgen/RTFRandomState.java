package org.haberno.terraloomed.worldgen;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.jetbrains.annotations.Nullable;

public interface RTFRandomState {
	void initialize(DynamicRegistryManager registryAccess);
	
	@Nullable
	DynamicRegistryManager registryAccess();
	
	@Nullable
	Preset preset();

	@Nullable
	GeneratorContext generatorContext();
	
	DensityFunction wrap(DensityFunction function);

	Noise wrap(Noise noise);
}
