package org.haberno.terraloomed.worldgen.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.AbstractConditionalPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;

class NoiseFilter extends AbstractConditionalPlacementModifier {
	public static final Codec<NoiseFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Noise.CODEC.fieldOf("noise").forGetter((filter) -> filter.noise),
		Codec.FLOAT.fieldOf("threshold").forGetter((filter) -> filter.threshold)
	).apply(instance, NoiseFilter::new));

	private RegistryEntry<Noise> noise;
	private float threshold;
	
	public NoiseFilter(RegistryEntry<Noise> noise, float threshold) {
		this.noise = noise;
		this.threshold = threshold;
	}
	
	@Override
	protected boolean shouldPlace(FeaturePlacementContext ctx, Random rand, BlockPos pos) {
		return this.noise.value().compute(pos.getX(), pos.getZ(), (int) ctx.getWorld().getSeed()) > this.threshold;
	}
	
	@Override
	public PlacementModifierType<NoiseFilter> getType() {
		return RTFPlacementModifiers.NOISE_FILTER;
	}
}
