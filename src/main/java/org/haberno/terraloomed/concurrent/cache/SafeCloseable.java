package org.haberno.terraloomed.concurrent.cache;

public interface SafeCloseable extends AutoCloseable {
    void close();
}
