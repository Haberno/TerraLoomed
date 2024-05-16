package org.haberno.terraloomed.worldgen.feature.chance;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;

import java.util.function.Function;

public interface ChanceModifier {
	public static final Codec<ChanceModifier> CODEC = RTFBuiltInRegistries.CHANCE_MODIFIER_TYPE.getCodec().dispatch(ChanceModifier::codec, Function.identity());
	
	float getChance(ChanceContext chanceCtx, FeatureContext<?> placeCtx);
	
	Codec<? extends ChanceModifier> codec();
}
