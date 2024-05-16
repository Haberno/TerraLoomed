package org.haberno.terraloomed.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import raccoonman.reterraforged.world.worldgen.biome.RTFClimateSampler;

@Mixin(MultiNoiseUtil.MultiNoiseSampler.class)
@Implements(@Interface(iface = RTFClimateSampler.class, prefix = "reterraforged$RTFClimateSampler$"))
class MixinClimateSampler {
	private BlockPos spawnSearchCenter = BlockPos.ORIGIN;
	
	public void reterraforged$RTFClimateSampler$setSpawnSearchCenter(BlockPos spawnSearchCenter) {
		this.spawnSearchCenter = spawnSearchCenter;
	}
	
	public BlockPos reterraforged$RTFClimateSampler$getSpawnSearchCenter() {
		return this.spawnSearchCenter;
	}
}
