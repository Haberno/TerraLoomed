package org.haberno.terraloomed.worldgen;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.Nullable;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;

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
