package org.haberno.terraloomed.worldgen.feature.template.buffer;

public interface BufferIterator {
    boolean isEmpty();

    boolean next();

    int nextIndex();
}