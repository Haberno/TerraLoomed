package org.haberno.terraloomed.worldgen.structure.rule;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;

import java.util.function.Function;

public interface StructureRule {
    public static final Codec<StructureRule> CODEC = RTFBuiltInRegistries.STRUCTURE_RULE_TYPE.getCodec().dispatch(StructureRule::codec, Function.identity());

	boolean test(NoiseConfig randomState, BlockPos pos);
	
	Codec<? extends StructureRule> codec();
}
