package org.haberno.terraloomed.mixin;

import com.mojang.datafixers.DataFixer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ChunkStatusChangeListener;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.level.storage.LevelStorage;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.WorldGenFlags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ThreadedAnvilChunkStorage.class)
public class MixinChunkMap {
	@Shadow
    private NoiseConfig randomState;
	
	@Inject(
		at = @At("TAIL"),
		method = "<init>"
	)
	public void ChunkMap(ServerWorld serverLevel, LevelStorage.Session storageAccess, DataFixer dataFixer, StructureTemplateManager templateLoader, Executor executor, ThreadExecutor<Runnable> eventLoop, ChunkProvider lightChunkGetter, ChunkGenerator chunkGenerator, WorldGenerationProgressListener chunkProgressListener, ChunkStatusChangeListener chunkStatusListener, Supplier<PersistentStateManager> dimensionStorage, int viewDistance, boolean syncChunkWrites, CallbackInfo callback) {
		if((Object) this.randomState instanceof RTFRandomState rtfRandomState) {
			WorldGenFlags.setCullNoiseSections(true);

			rtfRandomState.initialize(serverLevel.getRegistryManager());
		}
	}
}
