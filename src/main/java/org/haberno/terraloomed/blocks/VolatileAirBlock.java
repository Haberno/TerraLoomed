package org.haberno.terraloomed.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

//TODO
public class VolatileAirBlock extends AirBlock {

	protected VolatileAirBlock(Settings properties) {
		super(properties);
	}

	@Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState blockState2, WorldAccess levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return blockState;
    }
}
