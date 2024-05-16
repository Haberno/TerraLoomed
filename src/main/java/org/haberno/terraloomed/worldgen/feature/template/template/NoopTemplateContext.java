package org.haberno.terraloomed.worldgen.feature.template.template;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public record NoopTemplateContext() implements TemplateContext {

	@Override
	public void recordState(BlockPos pos, BlockState state) {
	}
}
