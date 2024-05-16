package org.haberno.terraloomed.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.level.ServerWorldProperties;
import raccoonman.reterraforged.data.preset.PresetData;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;
import raccoonman.reterraforged.world.worldgen.biome.RTFClimateSampler;

@Mixin(MinecraftServer.class)
class MixinMinecraftServer {

	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/biome/Climate$Sampler;findSpawnPosition()Lnet/minecraft/core/BlockPos;"
		),
		method = "setInitialSpawn"
	)
    private static void findSpawnPosition(ServerWorld serverLevel, ServerWorldProperties serverLevelData, boolean bl, boolean bl2, CallbackInfo callback) {
		NoiseConfig randomState = serverLevel.getChunkManager().getNoiseConfig();
		MultiNoiseUtil.MultiNoiseSampler sampler = randomState.getMultiNoiseSampler();
		serverLevel.getRegistryManager().getOptionalWrapper(RTFRegistries.PRESET).flatMap((registry) -> {
			return registry.get(PresetData.PRESET);
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
