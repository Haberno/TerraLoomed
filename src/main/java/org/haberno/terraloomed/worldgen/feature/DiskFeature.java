package org.haberno.terraloomed.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;

public class DiskFeature extends Feature<DiskFeatureConfig> {
    private static final Noise DOMAIN = Noises.simplex(1, 6, 3);

    public DiskFeature(Codec<DiskFeatureConfig> codec) {
		super(codec);
	}
    
	@Override
	public boolean generate(FeatureContext<DiskFeatureConfig> ctx) {
		StructureWorldAccess level = ctx.getWorld();
		Random random = ctx.getRandom();
		BlockPos pos = ctx.getOrigin();
		DiskFeatureConfig config = ctx.getConfig();
		ChunkGenerator generator = ctx.getGenerator();
		
        if (!level.getFluidState(pos).isIn(FluidTags.WATER)) {
            return false;
        } else {
            int cRadius = 6;
            int ySize = 5;

            int i = 0;
            int radius = 4 + random.nextInt(cRadius);
            float radius2 = (radius * radius)  * 0.65F;
            BlockPos.Mutable blockPos = new BlockPos.Mutable();

            for(int x = pos.getX() - radius; x <= pos.getX() + radius; ++x) {
                for(int z = pos.getZ() - radius; z <= pos.getZ() + radius; ++z) {
                    int dx = x - pos.getX();
                    int dz = z - pos.getZ();
                    float rad2 = DOMAIN.compute(x, z, 0) * radius2;
                    if (dx * dx + dz * dz <= rad2) {
                        for(int y = pos.getY() - ySize; y <= pos.getY() + ySize && y + 1 < generator.getWorldHeight(); ++y) {
                            blockPos.set(x, y, z);
                            
                            if(config.target().test(level, blockPos)) {
                                level.setBlockState(blockPos, config.stateProvider().getBlockState(level, random, blockPos), 2);
                                ++i;
                            }
                        }
                    }
                }
            }
            return i > 0;
        }
	}
}
