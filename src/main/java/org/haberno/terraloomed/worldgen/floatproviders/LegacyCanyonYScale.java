package org.haberno.terraloomed.worldgen.floatproviders;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.floatprovider.FloatProviderType;
import net.minecraft.util.math.random.Random;

@Deprecated
public class LegacyCanyonYScale extends FloatProvider {
	public static final Codec<LegacyCanyonYScale> CODEC = Codec.unit(LegacyCanyonYScale::new);
	
	@Override
	public float get(Random random) {
		return (random.nextFloat() - 0.5F) * 2.0F / 8.0F;
	}

	@Override
	public float getMin() {
		return -1.0F;
	}

	@Override
	public float getMax() {
		return 1.0F;
	}

	@Override
	public FloatProviderType<LegacyCanyonYScale> getType() {
		return RTFFloatProviderTypes.LEGACY_CANYON_Y_SCALE;
	}
}
