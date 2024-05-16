package org.haberno.terraloomed.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.haberno.terraloomed.worldgen.feature.SwampSurfaceFeature.Config;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.noise.module.Noises;

public class SwampSurfaceFeature extends Feature<Config> {
	private static final Noise MATERIAL_NOISE = makeMaterialNoise();
	
	public SwampSurfaceFeature(Codec<Config> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<Config> ctx) {
		Config config = ctx.getConfig();
		BlockPos origin = ctx.getOrigin();
		StructureWorldAccess level = ctx.getWorld();
		ChunkGenerator generator = ctx.getGenerator();
		Chunk chunk = level.getChunk(origin);
		BlockPos.Mutable pos = new BlockPos.Mutable();
		int waterY = generator.getSeaLevel() - 1;

		for(int localX = 0; localX < 16; localX++) {
			for(int localZ = 0; localZ < 16; localZ++) {
				int x = origin.getX() + localX;
				int z = origin.getZ() + localZ;
				int surfaceY = level.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
				
				double biomeInfoNoise = Biome.FOLIAGE_NOISE.sample(x * 0.25D, z * 0.25D, false);
				BlockState filler = getMaterial(x, waterY, z, waterY, config);

				pos.set(x, surfaceY, z);
				
				if(level.getBiome(pos).matchesKey(BiomeKeys.SWAMP)) {
			        if (biomeInfoNoise > 0.0D) {
			            for (int y = surfaceY; y >= surfaceY - 10; --y) {
			                pos.setY(y);
			                if (level.getBlockState(pos).isAir()) {
			                    continue;
			                }

			                if (y == waterY && !level.getFluidState(pos).isEmpty()) {
			                    level.setBlockState(pos, filler, 2);
			                }
			                break;
			            }
			        }

			        int oceanFloor = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, localX, localZ);
			        if (oceanFloor <= waterY) {
			        	level.setBlockState(pos.setY(oceanFloor), getMaterial(x, oceanFloor, z, waterY, config), 2);
			        }					
				}
			}	
		}
		return true;
	}

    private static BlockState getMaterial(int x, int y, int z, int waterY, Config config) {
        float value = MATERIAL_NOISE.compute(x, z, 0);
        if (value > 0.6F) {
            if (value < 0.75F && y < waterY) {
                return config.clayMaterial();
            }
            return config.gravelMaterial();
        }
        return config.dirtMaterial();
    }
    
    private static Noise makeMaterialNoise() {
    	Noise base = Noises.simplex(23, 40, 2);
    	return Noises.warpWhite(base, 213, 2, 4);    	
    }
    
    public record Config(BlockState clayMaterial, BlockState gravelMaterial, BlockState dirtMaterial) implements FeatureConfig {
    	public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    		BlockState.CODEC.fieldOf("clay_material").forGetter(Config::clayMaterial),
    		BlockState.CODEC.fieldOf("gravel_material").forGetter(Config::gravelMaterial),
    		BlockState.CODEC.fieldOf("dirt_material").forGetter(Config::dirtMaterial)
    	).apply(instance, Config::new));
    }
}
