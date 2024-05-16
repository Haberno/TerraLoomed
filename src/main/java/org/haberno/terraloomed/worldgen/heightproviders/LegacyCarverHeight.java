package org.haberno.terraloomed.worldgen.heightproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.heightprovider.HeightProviderType;

@Deprecated // pretty sure this can be replicated with UniformHeight
public class LegacyCarverHeight extends HeightProvider {
	public static final Codec<LegacyCarverHeight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("min").forGetter((h) -> h.min),
		Codec.INT.fieldOf("variation_min").forGetter((h) -> h.variationMin),
		Codec.INT.fieldOf("variation_range").forGetter((h) -> h.variationRange)
	).apply(instance, LegacyCarverHeight::new));
	
	private int min;
	private int variationMin;
	private int variationRange;
	
	private LegacyCarverHeight(int min, int variationMin, int variationRange) {
		this.min = min;
		this.variationMin = variationMin;
		this.variationRange = variationRange;
	}
	
	@Override
	public int get(Random random, HeightContext ctx) {
		return this.min + random.nextInt(this.variationMin + random.nextInt(this.variationRange));
	}

	@Override
	public HeightProviderType<LegacyCarverHeight> getType() {
		return RTFHeightProviderTypes.LEGACY_CARVER;
	}
	
	public static LegacyCarverHeight of(int min, int variationMin, int variationRange) {
		return new LegacyCarverHeight(min, variationMin, variationRange);
	}
}
