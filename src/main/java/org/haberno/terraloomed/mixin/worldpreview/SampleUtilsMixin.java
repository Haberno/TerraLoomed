package org.haberno.terraloomed.mixin.worldpreview;

import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.haberno.terraloomed.RTFCommon;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.WorldGenFlags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.nio.file.Path;

@Pseudo
@Mixin(targets = "caeruleusTait.world.preview.backend.worker.SampleUtils", remap = false)
public class SampleUtilsMixin {
	@Shadow 
	@Final
    private NoiseConfig randomState;
	@Shadow
	@Final
	private DynamicRegistryManager registryAccess;
	
	@Inject(
		at = @At("TAIL"),
		require = 1,
		target = @Desc(
			value = "<init>",
			args = { 
				MinecraftServer.class, BiomeSource.class, ChunkGenerator.class, GeneratorOptions.class, DimensionOptions.class, HeightLimitView.class
			}
		)
	)
	private void SampleUtils$1(CallbackInfo callback) {
		this.initRTF();
	}

	@Inject(
		at = @At("TAIL"), 
		require = 1,
		target = @Desc(
			value = "<init>",
			args = { 
				BiomeSource.class, ChunkGenerator.class, CombinedDynamicRegistries.class, GeneratorOptions.class, DimensionOptions.class, HeightLimitView.class, DataConfiguration.class, Proxy.class, Path.class 
			}
		)
	) 
	private void SampleUtils$2(CallbackInfo callback) {
		this.initRTF();
	}
	
	private void initRTF() {
		if((Object) this.randomState instanceof RTFRandomState rtfRandomState) {
			WorldGenFlags.setCullNoiseSections(false);
			
			rtfRandomState.initialize(this.registryAccess);
			
			RTFCommon.LOGGER.info("initialized rtf data");
		} else {
			throw new IllegalStateException();
		}
	}
}
