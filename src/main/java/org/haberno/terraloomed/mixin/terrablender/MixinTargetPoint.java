package org.haberno.terraloomed.mixin.terrablender;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import raccoonman.reterraforged.compat.terrablender.TBTargetPoint;

@Mixin(MultiNoiseUtil.NoiseValuePoint.class)
@Implements(@Interface(iface = TBTargetPoint.class, prefix = "reterraforged$TBTargetPoint$"))
class MixinTargetPoint {
	private double uniqueness = Double.NaN;

	public double reterraforged$TBTargetPoint$getUniqueness() {
		return this.uniqueness;
	}
	
	public void reterraforged$TBTargetPoint$setUniqueness(double uniqueness) {
		this.uniqueness = uniqueness;
	}
}
