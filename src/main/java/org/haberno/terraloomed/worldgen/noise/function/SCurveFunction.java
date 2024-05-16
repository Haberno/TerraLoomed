package org.haberno.terraloomed.worldgen.noise.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.haberno.terraloomed.worldgen.noise.NoiseUtil;

record SCurveFunction(float lower, float upper) implements CurveFunction {
	public static final Codec<SCurveFunction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.FLOAT.fieldOf("lower").forGetter(SCurveFunction::lower),
		Codec.FLOAT.fieldOf("upper").forGetter(SCurveFunction::upper)
	).apply(instance, SCurveFunction::new));
	
	public SCurveFunction {
		upper = upper < 0.0F ? Math.max(-lower, upper) : upper;
	}

	@Override
	public float apply(float f) {
        return NoiseUtil.pow(f, this.lower + this.upper * f);
	}

	@Override
	public Codec<SCurveFunction> codec() {
		return CODEC;
	}
}
