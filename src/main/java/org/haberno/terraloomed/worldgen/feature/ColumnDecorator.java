package org.haberno.terraloomed.worldgen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;

@Deprecated
public class ColumnDecorator {
	private static final Noise VARIANCE = Noises.perlin(0, 100, 1);

    public static void fillDownSolid(Chunk chunk, BlockPos.Mutable pos, int from, int to, BlockState state) {
        for (int dy = from; dy > to; dy--) { ;
            replaceSolid(chunk, pos.setY(dy), state);
        }
    }

    public static void replaceSolid(Chunk chunk, BlockPos pos, BlockState state) {
        if (chunk.getBlockState(pos).isAir()) {
            return;
        }
        chunk.setBlockState(pos, state, false);
    }
	
    public static float sampleNoise(float x, float z, float scale, float bias) {
        return VARIANCE.compute(x, z, 0) * scale + bias;
    }

    public static float sampleNoise(float x, float z, int scale, int bias) {
        return sampleNoise(x, z, scale / 255F, bias / 255F);
    }
}
