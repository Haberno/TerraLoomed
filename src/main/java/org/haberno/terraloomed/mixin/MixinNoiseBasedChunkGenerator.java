package org.haberno.terraloomed.mixin;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import raccoonman.reterraforged.data.preset.settings.WorldSettings;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;
import raccoonman.reterraforged.world.worldgen.WorldGenFlags;
import raccoonman.reterraforged.world.worldgen.cell.Cell;
import raccoonman.reterraforged.world.worldgen.noise.NoiseUtil;
import raccoonman.reterraforged.world.worldgen.surface.SurfaceRegion;

@Mixin(value = NoiseChunkGenerator.class, priority = 9001 /* we need this so we don't break noisium */)
class MixinNoiseBasedChunkGenerator {
	
	@Shadow
	@Final
    private RegistryEntry<ChunkGeneratorSettings> settings;
	
	@Inject(at = @At("HEAD"), method = "buildSurface", require = 1)
    public void buildSurface$HEAD(ChunkRegion worldGenRegion, StructureAccessor structureManager, NoiseConfig randomState, Chunk chunkAccess, CallbackInfo callback) {
		SurfaceRegion.set(worldGenRegion);
    }
	
	@Inject(at = @At("TAIL"), method = "buildSurface", require = 1)
    public void buildSurface$TAIL(ChunkRegion worldGenRegion, StructureAccessor structureManager, NoiseConfig randomState, Chunk chunkAccess, CallbackInfo callback) {
		SurfaceRegion.set(null);
    }
	
	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseSettings;height()I"
		),
		method = { "fillFromNoise", "populateNoise" }
	)
    public int fillFromNoise(GenerationShapeConfig settings, Executor executor, Blender blender, NoiseConfig randomState, StructureAccessor structureManager, Chunk chunkAccess2) {
		GeneratorContext generatorContext;
		ChunkPos chunkPos = chunkAccess2.getPos();
		if((Object) randomState instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
			return generatorContext.lookup.getGenerationHeight(chunkPos.x, chunkPos.z, this.settings.value(), true);
		} else {
    		return settings.height();
    	}
    }

	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseSettings;height()I"
		),
		require = 2,
		method = { "iterateNoiseColumn", "sampleHeightmap" }
	)
    private int iterateNoiseColumn(GenerationShapeConfig settings, HeightLimitView levelHeightAccessor, NoiseConfig randomState, int blockX, int blockZ, @Nullable MutableObject<VerticalBlockSample> mutableObject, @Nullable Predicate<BlockState> predicate) {
		GeneratorContext generatorContext;
		if((Object) randomState instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
			return generatorContext.lookup.getGenerationHeight(ChunkSectionPos.getSectionCoord(blockX), ChunkSectionPos.getSectionCoord(blockZ), this.settings.value(), !WorldGenFlags.fastLookups());
    	} else {
    		return settings.height();
    	}
    }
	
	@Inject(
		at = @At("TAIL"),
		method = "addDebugScreenInfo"
	)
    private void addDebugScreenInfo(List<String> list, NoiseConfig randomState, BlockPos blockPos, CallbackInfo callback) {
		@Nullable
		GeneratorContext generatorContext;
		if((Object) randomState instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
			Cell cell = new Cell();
			generatorContext.lookup.apply(cell, blockPos.getX(), blockPos.getZ());

			WorldSettings worldSettings = generatorContext.preset.world();
			WorldSettings.ControlPoints controlPoints = worldSettings.controlPoints;
			
			list.add("");
			list.add("Terrain Type: " + cell.terrain.getName());
			list.add("Terrain Region: " + cell.terrainRegionEdge);
			list.add("Terrain Mask: " + cell.terrainMask);
			list.add("Continent Edge: " + cell.continentEdge);
			list.add("Ground Variance: " + NoiseUtil.map(cell.continentNoise, controlPoints.coast, controlPoints.farInland));
			list.add("River Distance: " + cell.riverDistance);
			list.add("Mountain Chain: " + cell.mountainChainAlpha);
			list.add("Biome Type: " + cell.biomeType.name());
			list.add("Macro Biome: " + cell.macroBiomeId);
			list.add("Temperature: " + cell.regionTemperature);
			list.add("Moisture: " + cell.regionMoisture);
			list.add("");
    	}
    }
}
