package org.haberno.terraloomed.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public record NoiseSampler(RegistryEntry<org.haberno.terraloomed.worldgen.noise.module.Noise> noise, int seed) implements MappedFunction {

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
	
	public record Marker(RegistryEntry<org.haberno.terraloomed.worldgen.noise.module.Noise> noise) implements MappedFunction.Marker {
		public static final Codec<Marker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			org.haberno.terraloomed.worldgen.noise.module.Noise.CODEC.fieldOf("noise").forGetter(Marker::noise)
		).apply(instance, Marker::new));

		@Override
		public CodecHolder<Marker> getCodecHolder() {
			return new CodecHolder<>(CODEC);
		}

		@Override
		public DensityFunction apply(DensityFunctionVisitor visitor) {
			DensityFunction self = visitor instanceof org.haberno.terraloomed.worldgen.noise.module.Noise.Visitor noiseVisitor ?
				new Marker(RegistryEntry.of(this.noise.value().mapAll(noiseVisitor))) :
				this;
			return visitor.apply(self);
		}
	}
}
