package org.haberno.terraloomed.worldgen.feature.template.template;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

import java.util.function.IntFunction;

public abstract class BakedTransform<T> {
    public static final int MIRRORS = BlockMirror.values().length;
    public static final int ROTATIONS = BlockRotation.values().length;

    private final T[] backing;

    public BakedTransform(IntFunction<T[]> array, T value) {
        this.backing = array.apply(MIRRORS * ROTATIONS);
        for (BlockMirror mirror : BlockMirror.values()) {
            for (BlockRotation rotation : BlockRotation.values()) {
                T result = apply(mirror, rotation, value);
                this.backing[indexOf(mirror, rotation)] = result;
            }
        }
    }

    public T get(BlockMirror mirror, BlockRotation rotation) {
        return this.backing[indexOf(mirror, rotation)];
    }

    protected abstract T apply(BlockMirror mirror, BlockRotation rotation, T value);

    private static int indexOf(BlockMirror mirror, BlockRotation rotation) {
        return mirror.ordinal() * ROTATIONS + rotation.ordinal();
    }
}
