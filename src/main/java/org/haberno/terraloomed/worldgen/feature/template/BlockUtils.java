package org.haberno.terraloomed.worldgen.feature.template;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeature;

import java.util.function.BiPredicate;

public class BlockUtils {

    public static boolean isSoil(WorldAccess world, BlockPos pos) {
        return TreeFeature.isSoil(world.getBlockState(pos));
    }

    public static boolean isLeavesOrLogs(BlockState state) {
        return state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.LEAVES);
    }

    public static boolean isVegetation(WorldAccess world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isIn(BlockTags.SAPLINGS) || state.isIn(BlockTags.FLOWERS) || state.isOf(Blocks.VINE);
    }

    public static boolean canTreeReplace(WorldAccess world, BlockPos pos) {
        return TreeFeature.isAirOrLeaves(world, pos) || isVegetation(world, pos);
    }

    public static boolean isSolid(BlockView reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        return isSolid(state, reader, pos);
    }

    public static boolean isSolid(BlockState state, BlockView reader, BlockPos pos) {
        return state.isOpaque() || !state.canPathfindThrough(reader, pos, NavigationType.LAND);
    }

    public static boolean isSoilOrRock(WorldAccess world, BlockPos pos) {
        BlockState block = world.getBlockState(pos);
        return TreeFeature.isSoil(block) || block.isIn(BlockTags.BASE_STONE_OVERWORLD);
    }

    public static boolean isClearOverhead(WorldAccess world, BlockPos pos, int height, BiPredicate<WorldAccess, BlockPos> predicate) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        // world.getMaxHeight ?
        int max = Math.min(world.getTopY() - 1, pos.getY() + height);
        for (int y = pos.getY(); y < max; y++) {
            mutable.set(pos.getX(), y, pos.getZ());
            if (!predicate.test(world, mutable)) {
                return false;
            }
        }
        return true;
    }
}
