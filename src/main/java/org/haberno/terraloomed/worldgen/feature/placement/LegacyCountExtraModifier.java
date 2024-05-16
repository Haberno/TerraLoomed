package org.haberno.terraloomed.worldgen.feature.placement;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@Deprecated
class LegacyCountExtraModifier extends PlacementModifier {
	public static final Codec<LegacyCountExtraModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("count").forGetter((p) -> p.count),
		Codec.FLOAT.fieldOf("extra_chance").forGetter((p) -> p.extraChance),
		Codec.INT.fieldOf("extra_count").forGetter((p) -> p.extraCount)
	).apply(instance, LegacyCountExtraModifier::new));

	private int count;
	private float extraChance;
	private int extraCount;
	
	public LegacyCountExtraModifier(int count, float extraChance, int extraCount) {
		this.count = count;
		this.extraChance = extraChance;
		this.extraCount = extraCount;
	}
	
	@Override
	public Stream<BlockPos> getCount(FeaturePlacementContext ctx, Random random, BlockPos pos) {
	      int i = this.count + (random.nextFloat() < this.extraChance ? this.extraCount : 0);
	      return IntStream.range(0, i).mapToObj((o) -> {
	         return pos;
	      });
	}

	@Override
	public PlacementModifierType<LegacyCountExtraModifier> getType() {
		return RTFPlacementModifiers.LEGACY_COUNT_EXTRA;
	}
}
