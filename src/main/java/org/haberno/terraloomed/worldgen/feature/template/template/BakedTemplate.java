package org.haberno.terraloomed.worldgen.feature.template.template;

import java.util.stream.Stream;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

public class BakedTemplate extends BakedTransform<BlockInfo[]> {

    public BakedTemplate(BlockInfo[] value) {
        super(BlockInfo[][]::new, value);
    }

    @Override
    protected BlockInfo[] apply(BlockMirror mirror, BlockRotation rotation, BlockInfo[] value) {
        return Stream.of(value).map(block -> block.transform(mirror, rotation)).toArray(BlockInfo[]::new);
    }
}