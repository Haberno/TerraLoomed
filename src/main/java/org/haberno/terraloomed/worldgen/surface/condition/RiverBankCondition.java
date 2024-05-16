package org.haberno.terraloomed.worldgen.surface.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import raccoonman.reterraforged.world.worldgen.cell.Cell;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;

class RiverBankCondition extends ThresholdCondition {
	
	public RiverBankCondition(Context context, Noise threshold, Noise variance) {
		super(context, threshold, variance);
	}

	@Override
	protected float sample(Cell cell) {
		return 1.0F - cell.riverDistance;
	}
	
	public record Source(RegistryEntry<Noise> threshold, RegistryEntry<Noise> variance) implements MaterialRules.MaterialCondition {
		public static final Codec<Source> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Noise.CODEC.fieldOf("threshold").forGetter(Source::threshold),
			Noise.CODEC.fieldOf("variance").forGetter(Source::variance)
		).apply(instance, Source::new));

		@Override
		public RiverBankCondition apply(Context ctx) {
			return new RiverBankCondition(ctx, this.threshold.value(), this.variance.value());
		}

		@Override
		public CodecHolder<Source> codec() {
			return new CodecHolder<>(CODEC);
		}
	}
}
