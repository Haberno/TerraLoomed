package org.haberno.terraloomed.worldgen.feature.chance;

import java.util.function.Function;
import net.minecraft.world.gen.feature.util.FeatureContext;
import com.mojang.serialization.Codec;
import raccoonman.reterraforged.registries.RTFBuiltInRegistries;

public interface ChanceModifier {
	public static final Codec<ChanceModifier> CODEC = RTFBuiltInRegistries.CHANCE_MODIFIER_TYPE.byNameCodec().dispatch(ChanceModifier::codec, Function.identity());
	
	float getChance(ChanceContext chanceCtx, FeatureContext<?> placeCtx);
	
	Codec<? extends ChanceModifier> codec();
}
