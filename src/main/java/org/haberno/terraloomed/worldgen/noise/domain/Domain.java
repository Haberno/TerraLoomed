package org.haberno.terraloomed.worldgen.noise.domain;

import com.mojang.serialization.Codec;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;
import org.haberno.terraloomed.worldgen.noise.module.Noise;

import java.util.function.Function;

public interface Domain {
    public static final Codec<Domain> CODEC = RTFBuiltInRegistries.DOMAIN_TYPE.getCodec().dispatch(Domain::codec, Function.identity());
	
    float getOffsetX(float x, float z, int seed);
    
    float getOffsetZ(float x, float z, int seed);
    
    Domain mapAll(Noise.Visitor visitor);
    
    Codec<? extends Domain> codec();

    default float getX(float x, float z, int seed) {
        return x + this.getOffsetX(x, z, seed);
    }
    
    default float getZ(float x, float z, int seed) {
        return z + this.getOffsetZ(x, z, seed);
    }
}
