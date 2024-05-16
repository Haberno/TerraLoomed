package org.haberno.terraloomed.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColumnPos;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.WorldGenFlags;
import org.haberno.terraloomed.worldgen.densityfunction.CellSampler;
import org.haberno.terraloomed.worldgen.densityfunction.CellSampler.Cache2d;
import org.haberno.terraloomed.worldgen.tile.Tile;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkNoiseSampler.class)
class MixinNoiseChunk {
	private NoiseConfig randomState;
	private int chunkX, chunkZ;
	private int generationHeight;
	private Tile.Chunk chunk;
	private Cache2d cache2d;
    private ChunkGeneratorSettings generatorSettings;
	
	@Shadow
    @Final
    private DensityFunction initialDensityWithoutJaggedness;
    
	@Shadow
    @Final
	int startBiomeX;
	
	@Shadow
    @Final
    int startBiomeZ;
	
	@Shadow
    @Final
	private int horizontalCellCount;
	
	@Shadow
	private int verticalCellCount;
	
	@Shadow
    @Final
    private int verticalCellBlockCount;
	
	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/noise/NoiseConfig;getNoiseRouter()Lnet/minecraft/world/gen/noise/NoiseRouter;"
		),
		method = "<init>"
	)
	private NoiseRouter NoiseChunk(NoiseConfig randomState1, int cellCountXZ, NoiseConfig randomState2, int minBlockX, int minBlockZ, GenerationShapeConfig noiseSettings, DensityFunctionTypes.Beardifying beardifierOrMarker, ChunkGeneratorSettings noiseGeneratorSettings) {
		this.randomState = randomState1;
		this.chunkX = ChunkSectionPos.getSectionCoord(minBlockX);
		this.chunkZ = ChunkSectionPos.getSectionCoord(minBlockZ);
		this.generatorSettings = noiseGeneratorSettings;
		GeneratorContext generatorContext;
		if((Object) this.randomState instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
			boolean cache = !WorldGenFlags.fastLookups() || CellSampler.isCachedNoiseChunk(cellCountXZ);

//			if(beardifierOrMarker instanceof Beardifier beardifier && (beardifier.pieceIterator.hasNext() || beardifier.junctionIterator.hasNext())) {
//				this.generationHeight = noiseSettings.height();
//				System.out.println(this.generationHeight);
//			} else {
				this.generationHeight = generatorContext.lookup.getGenerationHeight(this.chunkX, this.chunkZ, noiseGeneratorSettings, cache);
//			}
			
			this.verticalCellCount = Math.min(this.verticalCellCount, this.generationHeight / this.verticalCellBlockCount);
			this.cache2d = new Cache2d();
			
			if(cache) {
				this.chunk = generatorContext.cache.provideAtChunk(this.chunkX, this.chunkZ).getChunkReader(this.chunkX, this.chunkZ);
			}
		} else {
			this.generationHeight = noiseSettings.height();
		}
		return this.randomState.getNoiseRouter();
	}
	
	@ModifyVariable(
		method = "<init>",
		at = @At("HEAD"),
		name = "fluidPicker",
		index = 7,
		ordinal = 0,
		argsOnly = true
	)
	//TODO clean this up
	private static AquiferSampler.FluidLevelSampler modifyFluidPicker(AquiferSampler.FluidLevelSampler fluidPicker, int cellCountXZ, NoiseConfig randomState, int minBlockX, int minBlockZ, GenerationShapeConfig noiseSettings, DensityFunctionTypes.Beardifying beardifierOrMarker, ChunkGeneratorSettings noiseGeneratorSettings) {
		if((Object) randomState instanceof RTFRandomState rtfRandomState) {
			@Nullable
			Preset preset = rtfRandomState.preset();
			@Nullable
			GeneratorContext generatorContext;
			if(preset != null && (generatorContext = rtfRandomState.generatorContext()) != null) {
				int lavaLevel = preset.world().properties.lavaLevel;
		        AquiferSampler.FluidLevel lava = new AquiferSampler.FluidLevel(lavaLevel, Blocks.LAVA.getDefaultState());
		        int seaLevel = noiseGeneratorSettings.seaLevel();
		        AquiferSampler.FluidLevel defaultFluid = new AquiferSampler.FluidLevel(seaLevel, noiseGeneratorSettings.defaultFluid());
		        return (x, y, z) -> {
		        	if (y < Math.min(lavaLevel, seaLevel)) {
		                return lava;
		            }
		            return defaultFluid;
		        };
			}
		}
		return fluidPicker;
	}

	@Inject(
		at = @At("HEAD"),
		method = "wrapNew",
		cancellable = true
	)
	private void wrapNew(DensityFunction function, CallbackInfoReturnable<DensityFunction> callback) {
		if((Object) this.randomState instanceof RTFRandomState randomState && function instanceof CellSampler mapped) {
			callback.setReturnValue(mapped.new CacheChunk(this.chunk, this.cache2d, this.chunkX, this.chunkZ));
		}
	}
	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseSettings;height()I"
		),
		require = 1,
		method = "computePreliminarySurfaceLevel"
	)
	private int computePreliminarySurfaceLevel(GenerationShapeConfig settings, long packedPos) {
        int blockX = ColumnPos.getX(packedPos);
        int blockZ = ColumnPos.getZ(packedPos);
        int generationHeight;
		GeneratorContext generatorContext;
        if((Object) this.randomState instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
        	generationHeight = generatorContext.lookup.getGenerationHeight(ChunkSectionPos.getSectionCoord(blockX), ChunkSectionPos.getSectionCoord(blockZ), this.generatorSettings, false);
        } else {
        	generationHeight = this.generatorSettings.generationShapeConfig().height();
        }
        return generationHeight;
	}
}