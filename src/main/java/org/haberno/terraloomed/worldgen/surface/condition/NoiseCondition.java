package org.haberno.terraloomed.worldgen.surface.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRuleContext;
import org.haberno.terraloomed.worldgen.noise.module.Noise;

public class NoiseCondition extends MaterialRules.HorizontalLazyAbstractPredicate {
	private Noise noise;
	private float threshold;
	
	private NoiseCondition(MaterialRuleContext context, Noise noise, float threshold) {
		super(context);
		
		this.noise = noise;
		this.threshold = threshold;
	}

	@Override
	protected boolean test() {
		return this.noise.compute(this.context.blockX, this.context.blockZ, 0) > this.threshold;
	}
	
	public record Source(RegistryEntry<Noise> noise, float threshold) implements MaterialRules.MaterialCondition {
		public static final Codec<Source> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Noise.CODEC.fieldOf("noise").forGetter(Source::noise),
			Codec.FLOAT.fieldOf("threshold").forGetter(Source::threshold)
		).apply(instance, Source::new));
		
		@Override
		public NoiseCondition apply(MaterialRuleContext ctx) {
			return new NoiseCondition(ctx, this.noise.value(), this.threshold);
		}

		@Override
		public CodecHolder<Source> codec() {
			return new CodecHolder<>(CODEC);
		}
	}
}
