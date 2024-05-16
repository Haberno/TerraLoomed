package org.haberno.terraloomed.worldgen.feature;

import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.haberno.map.worldgen.feature.ErodeSnowFeature.Config;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;
import raccoonman.reterraforged.world.worldgen.cell.Cell;
import raccoonman.reterraforged.world.worldgen.feature.ErodeSnowFeature.Config;
import raccoonman.reterraforged.world.worldgen.heightmap.Levels;
import raccoonman.reterraforged.world.worldgen.noise.NoiseUtil;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;
import raccoonman.reterraforged.world.worldgen.terrain.TerrainType;
import raccoonman.reterraforged.world.worldgen.tile.Tile;

public class ErodeSnowFeature extends Feature<org.haberno.map.worldgen.feature.ErodeSnowFeature.Config> {
    private static final float MIN = min(SnowBlock.LAYERS);
    private static final float MAX = max(SnowBlock.LAYERS);

	public ErodeSnowFeature(Codec<org.haberno.map.worldgen.feature.ErodeSnowFeature.Config> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<org.haberno.map.worldgen.feature.ErodeSnowFeature.Config> placeContext) {
		StructureWorldAccess level = placeContext.getWorld();
		NoiseConfig randomState = level.toServerWorld().getChunkManager().getNoiseConfig();
		
		@Nullable
		GeneratorContext generatorContext;
		if((Object) randomState instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
			ChunkGenerator generator = placeContext.getGenerator();
			ChunkPos chunkPos = new ChunkPos(placeContext.getOrigin());
			int chunkX = chunkPos.x;
			int chunkZ = chunkPos.z;
			Chunk chunk = level.getChunk(chunkX, chunkZ);
			Tile.Chunk tileChunk = generatorContext.cache.provideAtChunk(chunkX, chunkZ).getChunkReader(chunkX, chunkZ);
			raccoonman.reterraforged.world.worldgen.heightmap.Heightmap heightmap = generatorContext.localHeightmap.get();
			Levels levels = heightmap.levels();
			Noise rand = Noises.white(heightmap.climate().randomSeed(), 1);
			BlockPos.Mutable pos = new BlockPos.Mutable();
			org.haberno.map.worldgen.feature.ErodeSnowFeature.Config config = placeContext.getConfig();
			
			for(int x = 0; x < 16; x++) {
				for(int z = 0; z < 16; z++) {
		        	Cell cell = tileChunk.getCell(x, z);
		        	
					int scaledY = levels.scale(cell.height);
			        int surfaceY = chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
			        if(scaledY == surfaceY && scaledY >= generator.getSeaLevel() - 1) {
		        		int worldX = chunkPos.getOffsetX(x);
		        		int worldZ = chunkPos.getOffsetZ(z);
				        pos.set(worldX, surfaceY, worldZ);
				        
				        if(config.erode) {
//				        	if(level.getBiome(pos).value().getTemperature(pos) <= 0.25F) {
					            float var = -ColumnDecorator.sampleNoise(worldX, worldZ, 16, 0);
					            float hNoise = rand.compute(worldX, worldZ, 4) * config.heightModifier();
					            float sNoise = rand.compute(worldX, worldZ, 5) * config.slopeModifier();
					            float vModifier = cell.terrain == TerrainType.VOLCANO ? 0.15F : 0F;
					            float height = cell.height;// + var + hNoise + vModifier;
					            float steepness = cell.gradient;// + var + sNoise + vModifier;
					            
					            if (snowErosion(config, worldX, worldZ, steepness, height)) {
					                Predicate<BlockState> predicate = Heightmap.Type.MOTION_BLOCKING.getBlockPredicate();
					                for (int dy = 2; dy > 0; dy--) {
					                    pos.setY(surfaceY + dy);
					                    BlockState state = chunk.getBlockState(pos);
					                    if (!predicate.test(state) || state.isIn(BlockTags.SNOW)) {
							            	erodeSnow(chunk, pos);
					                    }
					                }
					            }
//					        }
				        }
				        
				        if(config.smooth) {
				            pos.setY(surfaceY + 1);

				            BlockState state = chunk.getBlockState(pos);
				            if (state.isAir()) {
				                pos.setY(surfaceY);
				                state = chunk.getBlockState(pos);
				                if (state.isAir()) {
				                    continue;
				                }
				            }

				            if(state.isOf(Blocks.SNOW)) {
				            	smoothSnow(chunk, pos, cell, levels, 0.0F);
				            }
				        }
			        }
				}
			}
	        return true;
		} else {
			throw new IllegalStateException();
		}
	}

    private static boolean snowErosion(org.haberno.map.worldgen.feature.ErodeSnowFeature.Config config, float x, float z, float steepness, float height) {
        return /*steepness > erodeConfig.rockSteepness() ||*/ (steepness * 0.55F > config.steepness());// && height > config.height() || (steepness > erodeConfig.dirtSteepness() && height > ColumnDecorator.sampleNoise(x, z, erodeConfig.dirtVar(), erodeConfig.dirtMin()));
    }

    private static void erodeSnow(Chunk chunk, BlockPos.Mutable pos) {
        chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);

        if (pos.getY() > 0) {
            pos.setY(pos.getY() - 1);
            BlockState below = chunk.getBlockState(pos);
            if (below.contains(GrassBlock.SNOWY)) {
                chunk.setBlockState(pos, below.with(GrassBlock.SNOWY, false), false);
            }
        }
    }
    
    private static void smoothSnow(Chunk chunk, BlockPos.Mutable pos, Cell cell, Levels levels, float min) {
        float height = cell.height * levels.worldHeight;
        float depth = getDepth(height);
        if (depth > min) {
            int level = getLevel(depth);
            BlockState layer = getState(level);
            if (layer.isOf(Blocks.AIR)) {
                return;
            }
            chunk.setBlockState(pos, layer, false);

           fixBaseBlock(chunk, pos, layer, level);
        }
    }

    private static void fixBaseBlock(Chunk chunk, BlockPos.Mutable pos, BlockState layerMaterial, int level) {
        if (layerMaterial.isOf(Blocks.SNOW)) {
            pos.move(Direction.DOWN);
            BlockState belowState = chunk.getBlockState(pos);

            if(level > 1 && belowState.getBlock() instanceof SpreadableBlock) {
                chunk.setBlockState(pos, Blocks.DIRT.getDefaultState(), false);
            } else if(level > 0) {
                chunk.setBlockState(pos, Blocks.SNOW_BLOCK.getDefaultState(), false);
            }
        }
    }

    private static BlockState getState(int level) {
        if (level < MIN) {
            return Blocks.AIR.getDefaultState();
        }
        if (level >= MAX) {
            return Blocks.SNOW_BLOCK.getDefaultState();
        }
        return Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, level);
    }
    
    private static int getLevel(float depth) {
        if (depth > 1) {
            depth = getDepth(depth);
        } else if (depth < 0) {
            depth = 0;
        }
        return NoiseUtil.round(depth * MAX);
    }

    private static float getDepth(float height) {
        return height - (int) height;
    }

    private static int min(Property<Integer> property) {
        return property.getValues().stream().min(Integer::compareTo).orElse(0);
    }

    private static int max(Property<Integer> property) {
        return property.getValues().stream().max(Integer::compareTo).orElse(0);
    }

	public record Config(float steepness, float height, boolean erode, boolean smooth, float slopeModifier, float heightModifier) implements FeatureConfig {
		public static final Codec<org.haberno.map.worldgen.feature.ErodeSnowFeature.Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("steepness").forGetter(org.haberno.map.worldgen.feature.ErodeSnowFeature.Config::steepness),
			Codec.FLOAT.fieldOf("height").forGetter(org.haberno.map.worldgen.feature.ErodeSnowFeature.Config::height),
			Codec.BOOL.fieldOf("erode").forGetter(org.haberno.map.worldgen.feature.ErodeSnowFeature.Config::erode),
			Codec.BOOL.fieldOf("smooth").forGetter(org.haberno.map.worldgen.feature.ErodeSnowFeature.Config::smooth),
			Codec.FLOAT.fieldOf("slope_modifier").forGetter(org.haberno.map.worldgen.feature.ErodeSnowFeature.Config::slopeModifier),
			Codec.FLOAT.fieldOf("height_modifier").forGetter(org.haberno.map.worldgen.feature.ErodeSnowFeature.Config::heightModifier)
		).apply(instance, org.haberno.map.worldgen.feature.ErodeSnowFeature.Config::new));
	}
}
