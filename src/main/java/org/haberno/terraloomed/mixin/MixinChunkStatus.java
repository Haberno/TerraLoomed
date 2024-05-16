package org.haberno.terraloomed.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.util.Either;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;
import raccoonman.reterraforged.world.worldgen.WorldGenFlags;

@Mixin(ChunkStatus.class)
public class MixinChunkStatus {

	//structure starts
	@Inject(
		at = @At("HEAD"),
		method = "method_39464",
		remap = false
	)
	private static void method_39464$HEAD(ChunkStatus status, Executor executor, ServerWorld level, ChunkGenerator generator, StructureTemplateManager templateManager, ServerLightingProvider lightEngine, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> chunkLookup, List<Chunk> regionChunks, Chunk centerChunk, CallbackInfoReturnable<CompletableFuture<Chunk>> callback) {
		NoiseConfig randomState = level.getChunkManager().getNoiseConfig();
		if((Object) randomState instanceof RTFRandomState rtfRandomState) {
			ChunkPos chunkPos = centerChunk.getPos();
			@Nullable
			GeneratorContext context = rtfRandomState.generatorContext();
			
			if(context != null) {
				context.cache.queueAtChunk(chunkPos.x, chunkPos.z);

				WorldGenFlags.setFastCellLookups(false);
			}
		}
	}
	
	@Inject(
		at = @At("TAIL"),
		method = "method_39464",
		remap = false
	)
	private static void method_39464$TAIL(ChunkStatus status, Executor executor, ServerWorld level, ChunkGenerator generator, StructureTemplateManager templateManager, ServerLightingProvider lightEngine, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> chunkLookup, List<Chunk> regionChunks, Chunk centerChunk, CallbackInfoReturnable<CompletableFuture<Chunk>> callback) {
		NoiseConfig randomState = level.getChunkManager().getNoiseConfig();
		if((Object) randomState instanceof RTFRandomState rtfRandomState) {
			@Nullable
			GeneratorContext context = rtfRandomState.generatorContext();
			if(context != null) {
				WorldGenFlags.setFastCellLookups(true);
			}
		}
	}
	
	//features
	@Inject(
		at = @At("TAIL"),
		method = "method_51375",
		remap = false
	)
	private static void method_51375(ChunkStatus status, ServerWorld level, ChunkGenerator generator, List<Chunk> chunks, Chunk centerChunk, CallbackInfo callback) {
		NoiseConfig randomState = level.getChunkManager().getNoiseConfig();
		if((Object) randomState instanceof RTFRandomState rtfRandomState) {
			ChunkPos chunkPos = centerChunk.getPos();
			@Nullable
			GeneratorContext context = rtfRandomState.generatorContext();
			
			if(context != null) {
				context.cache.dropAtChunk(chunkPos.x, chunkPos.z);
			}
		}
	}
}
