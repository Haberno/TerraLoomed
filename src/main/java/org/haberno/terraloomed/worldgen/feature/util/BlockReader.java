package org.haberno.terraloomed.worldgen.feature.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Deprecated(forRemoval = true)
public class BlockReader implements BlockView {
	private BlockState state;
	
	public BlockReader setState(BlockState state) {
		this.state = state;
		return this;
	}
	
	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getBottomY() {
		return 0;
	}

	@Override
	public BlockEntity getBlockEntity(BlockPos var1) {
        return null;
	}

	@Override
	public BlockState getBlockState(BlockPos var1) {
		return this.state;
	}

	@Override
	public FluidState getFluidState(BlockPos var1) {
		return Fluids.EMPTY.getDefaultState();
	}
}
