package org.haberno.terraloomed.worldgen.feature.template.template;

import net.minecraft.block.BlockState;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;

public record BlockInfo(BlockPos pos, BlockState state) {
	
    public BlockInfo transform(BlockMirror mirror, BlockRotation rotation) {
        BlockPos pos = FeatureTemplate.transform(this.pos, mirror, rotation);
        BlockState state = this.state.mirror(mirror).rotate(rotation);
        return new BlockInfo(pos, state);
    }

    @Override
    public String toString() {
        return this.state.toString();
    }
}
