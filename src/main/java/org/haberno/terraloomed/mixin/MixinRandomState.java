package org.haberno.terraloomed.mixin;

import com.google.common.base.Suppliers;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import org.haberno.terraloomed.RTFCommon;
import org.haberno.terraloomed.compat.terrablender.TBClimateSampler;
import org.haberno.terraloomed.compat.terrablender.TBCompat;
import org.haberno.terraloomed.compat.terrablender.TBNoiseRouterData;
import org.haberno.terraloomed.concurrent.ThreadPools;
import org.haberno.terraloomed.concurrent.cache.CacheManager;
import org.haberno.terraloomed.config.PerformanceConfig;
import org.haberno.terraloomed.data.preset.PresetData;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.densityfunction.CellSampler;
import org.haberno.terraloomed.worldgen.densityfunction.NoiseSampler;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.noise.module.Noises;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NoiseConfig.class)
@Implements(@Interface(iface = RTFRandomState.class, prefix = "reterraforged$RTFRandomState$"))
class MixinRandomState {
	@Shadow
	@Final
	private MultiNoiseUtil.MultiNoiseSampler sampler;
	@Shadow
	@Final
    private SurfaceBuilder surfaceSystem;

	@Nullable
	private Preset preset;
	@Nullable
	private NoiseSampler globalSampler;
	@Nullable
	private DynamicRegistryManager registryAccess;
	
	@Deprecated
	private boolean hasContext;
	@Deprecated
	@Nullable
	private GeneratorContext generatorContext;
	
	private long seed;
	private DensityFunction.DensityFunctionVisitor densityFunctionWrapper;
	
	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/noise/NoiseRouter;apply(Lnet/minecraft/world/gen/densityfunction/DensityFunction$DensityFunctionVisitor;)Lnet/minecraft/world/gen/noise/NoiseRouter;"
		),
		method = "<init>",
		require = 1
	)
	private NoiseRouter RandomState(NoiseRouter router, DensityFunction.DensityFunctionVisitor visitor, ChunkGeneratorSettings noiseGeneratorSettings, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> params, final long seed) {
		this.seed = seed;
		this.densityFunctionWrapper = new DensityFunction.DensityFunctionVisitor() {
			
			@Override
			public DensityFunction apply(DensityFunction function) {
				if(function instanceof NoiseSampler.Marker marker) {
					return new NoiseSampler(marker.noise(), (int) seed);
				}
				if(function instanceof CellSampler.Marker marker) {
					MixinRandomState.this.hasContext |= true;
					return new CellSampler(Suppliers.memoize(() -> MixinRandomState.this.generatorContext), marker.field());
				}
				return visitor.apply(function);
			}

			@Override
			public DensityFunction.Noise apply(DensityFunction.Noise noiseHolder) {
	            return visitor.apply(noiseHolder);
	        }
		};
		return router.apply(this.densityFunctionWrapper);
	}

	public void reterraforged$RTFRandomState$initialize(DynamicRegistryManager registryAccess) {
		this.registryAccess = registryAccess;
		
		Impl<Preset> presets = registryAccess.getWrapperOrThrow(RTFRegistries.PRESET);
		Impl<DensityFunction> functions = registryAccess.getWrapperOrThrow(RegistryKeys.DENSITY_FUNCTION);
		
		if((Object) this.sampler instanceof TBClimateSampler tbClimateSampler && TBCompat.isEnabled()) {
			functions.getOptional(TBNoiseRouterData.UNIQUENESS).ifPresent((uniqueness) -> {
				tbClimateSampler.setUniqueness(uniqueness.value().apply(this.densityFunctionWrapper));
			});
		}
		
		presets.getOptional(PresetData.PRESET).ifPresent((presetHolder) -> {
			this.preset = presetHolder.value();

			if(this.hasContext) {
				//TODO move this somewhere else
				CacheManager.clear();
				
				PerformanceConfig config = PerformanceConfig.read(PerformanceConfig.DEFAULT_FILE_PATH)
					.resultOrPartial(RTFCommon.LOGGER::error)
					.orElseGet(PerformanceConfig::makeDefault);
				this.generatorContext = GeneratorContext.makeCached(this.preset, (int) this.seed, config.tileSize(), config.batchCount(), ThreadPools.availableProcessors() > 4);
			}
		});
	}
	
	@Nullable
	public DynamicRegistryManager reterraforged$RTFRandomState$registryAccess() {
		return this.registryAccess;
	}
	
	@Nullable
	public Preset reterraforged$RTFRandomState$preset() {
		return this.preset;
	}
	
	@Nullable
	public GeneratorContext reterraforged$RTFRandomState$generatorContext() {
		return this.generatorContext;
	}

	@Nullable
	public DensityFunction reterraforged$RTFRandomState$wrap(DensityFunction function) {
		return function.apply(this.densityFunctionWrapper);
	}

	public Noise reterraforged$RTFRandomState$wrap(Noise noise) {
		return Noises.shiftSeed(noise, (int) this.seed);
	}
}
