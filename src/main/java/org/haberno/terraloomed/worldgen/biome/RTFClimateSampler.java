package org.haberno.terraloomed.worldgen.biome;

import net.minecraft.util.math.BlockPos;

public interface RTFClimateSampler {
	void setSpawnSearchCenter(BlockPos center);
	
	BlockPos getSpawnSearchCenter();
}
