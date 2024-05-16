package org.haberno.terraloomed.compat.terrablender;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.Nullable;

public interface TBClimateSampler {
	void setSpawnSearchCenter(BlockPos center);
	
	void setUniqueness(DensityFunction function);
	
	@Nullable
	DensityFunction getUniqueness();
}
