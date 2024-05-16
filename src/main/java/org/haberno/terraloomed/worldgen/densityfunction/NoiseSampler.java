package org.haberno.terraloomed.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;

public record NoiseSampler(RegistryEntry<Noise> noise, int seed) implements MappedFunction {

	@Override
	public double sample(NoisePos ctx) {
		return this.noise.value().compute(ctx.blockX(), ctx.blockZ(), this.seed);
	}

	@Override
	public double minValue() {
		return this.noise.value().minValue();
	}

	@Override
	public double maxValue() {
		return this.noise.value().maxValue();
	}
	
	public record Marker(RegistryEntry<Noise> noise) implements MappedFunction.Marker {
		public static final Codec<org.haberno.map.worldgen.densityfunction.NoiseSampler.Marker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Noise.CODEC.fieldOf("noise").forGetter(org.haberno.map.worldgen.densityfunction.NoiseSampler.Marker::noise)
		).apply(instance, org.haberno.map.worldgen.densityfunction.NoiseSampler.Marker::new));

		@Override
		public CodecHolder<org.haberno.map.worldgen.densityfunction.NoiseSampler.Marker> getCodecHolder() {
			return new CodecHolder<>(REGISTRY_ENTRY_CODEC);
		}

		@Override
		public DensityFunction apply(DensityFunctionVisitor visitor) {
			DensityFunction self = visitor instanceof Noise.Visitor noiseVisitor ?
				new org.haberno.map.worldgen.densityfunction.NoiseSampler.Marker(RegistryEntry.of(this.noise.value().mapAll(noiseVisitor))) : 
				this;
			return visitor.apply(self);
		}
	}
}
