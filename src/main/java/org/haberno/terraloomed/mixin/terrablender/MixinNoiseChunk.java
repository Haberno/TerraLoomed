package org.haberno.terraloomed.mixin.terrablender;

import java.util.List;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import raccoonman.reterraforged.compat.terrablender.TBClimateSampler;

@Mixin(ChunkNoiseSampler.class)
public class MixinNoiseChunk {
	private NoiseConfig randomState;

	@Inject(
		at = @At("TAIL"),
		method = "<init>"
	)
	private void NoiseChunk(int cellCount, NoiseConfig randomState, int x, int z, GenerationShapeConfig noiseSettings, DensityFunctionTypes.Beardifying beardifierOrMarker, ChunkGeneratorSettings noiseGeneratorSettings, AquiferSampler.FluidLevelSampler fluidPicker, Blender blender, CallbackInfo callback) {
		this.randomState = randomState;
	}
	
	@Inject(
		at = @At("RETURN"),
		method = "cachedClimateSampler"
	)
	private void cachedClimateSampler(NoiseRouter noiseRouter, List<MultiNoiseUtil.NoiseHypercube> list, CallbackInfoReturnable<MultiNoiseUtil.MultiNoiseSampler> callback) {
    	if((Object) callback.getReturnValue() instanceof TBClimateSampler cachedSampler && (Object) this.randomState.getMultiNoiseSampler() instanceof TBClimateSampler globalSampler) {
    		DensityFunction uniqueness = globalSampler.getUniqueness();

    		if(uniqueness != null) {
    			cachedSampler.setUniqueness(this.wrap(uniqueness));
    		}
    	}
    }

	@Shadow
    private DensityFunction wrap(DensityFunction densityFunction) {
		throw new IllegalStateException();
    }
}
