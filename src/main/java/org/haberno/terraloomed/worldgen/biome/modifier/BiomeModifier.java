package org.haberno.terraloomed.worldgen.biome.modifier;

import com.mojang.serialization.Codec;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;

import java.util.function.Function;

public interface BiomeModifier {
    public static final Codec<BiomeModifier> CODEC = RTFBuiltInRegistries.BIOME_MODIFIER_TYPE.getCodec().dispatch(BiomeModifier::codec, Function.identity());
    
	Codec<? extends BiomeModifier> codec();
}
