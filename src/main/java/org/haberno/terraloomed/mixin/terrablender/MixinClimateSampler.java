package org.haberno.terraloomed.mixin.terrablender;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseValuePoint;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import raccoonman.reterraforged.compat.terrablender.TBClimateSampler;
import raccoonman.reterraforged.compat.terrablender.TBTargetPoint;

@Mixin(MultiNoiseUtil.MultiNoiseSampler.class)
@Implements(@Interface(iface = TBClimateSampler.class, prefix = "reterraforged$TBClimateSampler$"))
class MixinClimateSampler {
	@Nullable
	private DensityFunction uniqueness;
	
	@Inject(
		at = @At("RETURN"), 
		method = "sample",
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void sample(int i, int j, int k, CallbackInfoReturnable<NoiseValuePoint> callback, int l, int m, int n, DensityFunction.UnblendedNoisePos ctx) {
		if(this.uniqueness != null && (Object) callback.getReturnValue() instanceof TBTargetPoint tbTargetPoint) {
			tbTargetPoint.setUniqueness(this.uniqueness.sample(ctx));
		}
	}
	
	public void reterraforged$TBClimateSampler$setUniqueness(DensityFunction uniqueness) {
		this.uniqueness = uniqueness;
	}
	
	public DensityFunction reterraforged$TBClimateSampler$getUniqueness() {
		return this.uniqueness;
	}
}
