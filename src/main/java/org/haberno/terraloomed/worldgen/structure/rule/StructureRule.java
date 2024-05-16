package org.haberno.terraloomed.worldgen.structure.rule;

import java.util.function.Function;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.noise.NoiseConfig;
import com.mojang.serialization.Codec;
import raccoonman.reterraforged.registries.RTFBuiltInRegistries;

public interface StructureRule {
    public static final Codec<StructureRule> CODEC = RTFBuiltInRegistries.STRUCTURE_RULE_TYPE.byNameCodec().dispatch(StructureRule::codec, Function.identity());

	boolean test(NoiseConfig randomState, BlockPos pos);
	
	Codec<? extends StructureRule> codec();
}
