package org.haberno.terraloomed.concurrent;

public interface Disposable {
    void dispose();
    
    public interface Listener<T> {
        void onDispose(T ctx);
    }
}
