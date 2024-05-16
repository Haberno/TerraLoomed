package org.haberno.terraloomed.worldgen.noise.function;

import com.mojang.serialization.Codec;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;

import java.util.function.Function;

public interface CurveFunction {
    public static final Codec<CurveFunction> CODEC = RTFBuiltInRegistries.CURVE_FUNCTION_TYPE.getCodec().dispatch(CurveFunction::codec, Function.identity());
	
	float apply(float f);
	
	Codec<? extends CurveFunction> codec();
}
