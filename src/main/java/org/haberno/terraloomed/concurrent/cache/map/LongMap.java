package org.haberno.terraloomed.concurrent.cache.map;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Predicate;

public interface LongMap<T> {
    int size();
    
    void clear();
    
    void remove(long key);
    
    void remove(long key, Consumer<T> ifPreset);
    
    int removeIf(Predicate<T> predicate);
    
    void put(long key, T value);
    
    T get(long key);
    
    T computeIfAbsent(long key, LongFunction<T> computer);
    
    default <V> V map(long key, LongFunction<T> factory, Function<T, V> mapper) {
        return mapper.apply(this.computeIfAbsent(key, factory));
    }
}
