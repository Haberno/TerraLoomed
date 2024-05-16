package org.haberno.terraloomed.worldgen.densityfunction;

import net.minecraft.world.gen.densityfunction.DensityFunction.NoisePos;

public class MutableFunctionContext implements NoisePos {
	private int blockX, blockY, blockZ;

	public MutableFunctionContext at(int x, int y, int z) {
		this.blockX = x;
		this.blockY = y;
		this.blockZ = z;
		return this;
	}
	
	@Override
	public int blockX() {
		return this.blockX;
	}

	@Override
	public int blockY() {
		return this.blockY;
	}

	@Override
	public int blockZ() {
		return this.blockZ;
	}
}