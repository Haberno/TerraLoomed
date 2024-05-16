package org.haberno.terraloomed.mixin;

import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import raccoonman.reterraforged.world.worldgen.biome.spawn.SpawnFinderFix;

// FIXME the mixin that we should actually be using just refuses to work for some reason

//@Mixin(targets = "net.minecraft.world.level.biome.Climate$SpawnFinder")
@Mixin(MultiNoiseUtil.class)
class MixinSpawnFinder {

	@Inject(at = @At("HEAD"), method = "findSpawnPosition", cancellable = true)
    private static void findSpawnPosition(List<NoiseHypercube> list, MultiNoiseSampler sampler, CallbackInfoReturnable<BlockPos> callback) {
    	callback.setReturnValue(new SpawnFinderFix(list, sampler).result.location());
    }
//	@ModifyArg(
//		at = @At(
//			value = "INVOKE",
//			target = "Lnet/minecraft/world/level/biome/Climate$SpawnFinder;getSpawnPositionAndFitness(Ljava/util/List;Lnet/minecraft/world/level/biome/Climate$Sampler;II)Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;"
//		),
//		method = "<init>",
//		index = 2,
//		require = 1
//	)
//	private static int modifyX(List<ParameterPoint> points, Climate.Sampler sampler, int x, int z) {
//		if((Object) sampler instanceof RTFClimateSampler rtfClimateSampler) {
//			BlockPos center = rtfClimateSampler.getSpawnSearchCenter();
//			return center != null ? center.getX() : 0;
//		} else {
//			return 0;
//		}
//	}
//	
//	@ModifyArg(
//		at = @At(
//			value = "INVOKE",
//			target = "Lnet/minecraft/world/level/biome/Climate$SpawnFinder;getSpawnPositionAndFitness(Ljava/util/List;Lnet/minecraft/world/level/biome/Climate$Sampler;II)Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;"
//		),
//		method = "<init>",
//		index = 3,
//		require = 1
//	)
//	private static int modifyZ(List<ParameterPoint> points, Climate.Sampler sampler, int x, int z) {
//		if((Object) sampler instanceof RTFClimateSampler rtfClimateSampler) {
//			BlockPos center = rtfClimateSampler.getSpawnSearchCenter();
//			return center != null ? center.getZ() : 0;
//		} else {
//			return 0;
//		}
//	}
}