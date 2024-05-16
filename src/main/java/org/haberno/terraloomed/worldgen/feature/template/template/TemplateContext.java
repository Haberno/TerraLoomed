package org.haberno.terraloomed.worldgen.feature.template.template;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface TemplateContext {
	void recordState(BlockPos pos, BlockState state);
	
    public interface Factory<T extends TemplateContext> {
    	T createContext();
    }
}
