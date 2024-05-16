package org.haberno.terraloomed.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.level.ServerWorldProperties;
import org.haberno.terraloomed.data.preset.PresetData;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.biome.RTFClimateSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
class MixinMinecraftServer {

	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$MultiNoiseSampler;findBestSpawnPosition()Lnet/minecraft/util/math/BlockPos;"
		),
		method = "setupSpawn"
	)

    private static void findBestSpawnPosition(ServerWorld serverLevel, ServerWorldProperties serverLevelData, boolean bl, boolean bl2, CallbackInfo callback) {
		NoiseConfig randomState = serverLevel.getChunkManager().getNoiseConfig();
		MultiNoiseUtil.MultiNoiseSampler sampler = randomState.getMultiNoiseSampler();
		serverLevel.getRegistryManager().getOptionalWrapper(RTFRegistries.PRESET).flatMap((registry) -> {
			return registry.getOptional(PresetData.PRESET);
		}).ifPresent((preset) -> {
			if((Object) randomState instanceof RTFRandomState rtfRandomState && (Object) sampler instanceof RTFClimateSampler rtfClimateSampler) {
				BlockPos searchCenter = preset.value().world().properties.spawnType.getSearchCenter(rtfRandomState.generatorContext());
				rtfClimateSampler.setSpawnSearchCenter(searchCenter);
			} else {
				throw new IllegalStateException();
			}
		});
    }
}
