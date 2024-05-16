package org.haberno.terraloomed.concurrent.cache;

public interface ExpiringEntry {
    long getTimestamp();
    
    default void close() {
    }
}
