package org.haberno.terraloomed.worldgen.noise.domain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.haberno.terraloomed.worldgen.noise.NoiseUtil;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.noise.module.Noise.Visitor;

record DirectionWarp(Noise direction, Noise strength) implements Domain {
	public static final Codec<DirectionWarp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Noise.HOLDER_HELPER_CODEC.fieldOf("direction").forGetter(DirectionWarp::direction),
		Noise.HOLDER_HELPER_CODEC.fieldOf("strength").forGetter(DirectionWarp::strength)
	).apply(instance, DirectionWarp::new));
	
	@Override
	public float getOffsetX(float x, float z, int seed) {
        float angle = this.direction.compute(x, z, seed) * 6.2831855F;
        return NoiseUtil.sin(angle) * this.strength.compute(x, z, seed);
	}

	@Override
	public float getOffsetZ(float x, float z, int seed) {
        float angle = this.direction.compute(x, z, seed) * 6.2831855F;
        return NoiseUtil.cos(angle) * this.strength.compute(x, z, seed);
	}

	@Override
	public Domain mapAll(Visitor visitor) {
		return new DirectionWarp(this.direction.mapAll(visitor), this.strength.mapAll(visitor));
	}

	@Override
	public Codec<DirectionWarp> codec() {
		return CODEC;
	}
}
