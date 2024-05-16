package org.haberno.terraloomed.mixin.terrablender;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.haberno.terraloomed.compat.terrablender.TBTargetPoint;
import org.haberno.terraloomed.worldgen.noise.NoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import terrablender.api.RegionType;
import terrablender.api.Regions;

@Mixin(targets = "terrablender.mixin.MixinParameterList")
class MixinParameterList<T> {
	private int maxIndex;

	@Inject(
		at = @At("HEAD"),
		method = "initializeForTerraBlender"
	)
    public void initializeForTerraBlender(DynamicRegistryManager registryAccess, RegionType regionType, long seed, CallbackInfo callback) {
    	this.maxIndex = Regions.getCount(regionType) - 1;
//
//    	registryAccess.lookup(RTFRegistries.PRESET).flatMap((registry) -> {
//    		return registry.get(Preset.KEY);
//    	}).ifPresent((holder) -> {
//    		Preset preset = holder.value();
//        	TBCompat.setSurfaceRules(preset, (defaultRules) -> {
//        		return RTFSurfaceRuleData.overworld(preset, registryAccess.lookupOrThrow(Registries.DENSITY_FUNCTION), registryAccess.lookupOrThrow(RTFRegistries.NOISE), defaultRules);
//            });
//    	});
    }

	@Redirect(
		method = "findValuePositional",
		at = @At(
			value = "INVOKE",
			target =  "Lterrablender/mixin/MixinParameterList;getUniqueness(III)I"
		)
	)
    public int getUniqueness(MultiNoiseUtil.Entries<T> parameterList, int x, int y, int z, MultiNoiseUtil.NoiseValuePoint targetPoint) {
		if((Object) targetPoint instanceof TBTargetPoint tbTargetPoint) {
			double uniqueness = tbTargetPoint.getUniqueness();
			if(Double.isNaN(uniqueness)) {
				return this.getUniqueness(x, y, z);
			}
			return NoiseUtil.round(this.maxIndex * (float) uniqueness);
		} else {
			throw new IllegalStateException();
		}
    }

	@Shadow
    public int getUniqueness(int x, int y, int z) {
    	throw new UnsupportedOperationException();
    }
}
