package org.haberno.terraloomed.concurrent.cache;


import org.haberno.terraloomed.concurrent.cache.map.LongMap;
import org.haberno.terraloomed.concurrent.cache.map.StampedBoundLongMap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

public class CacheManager {
	private static final List<Cache<?>> CACHES = Collections.synchronizedList(new LinkedList<>());
	
	public static <V extends ExpiringEntry> Cache<V> createCache(int capacity, long expireTime, long pollInterval, TimeUnit unit) {
		return createCache(capacity, expireTime, pollInterval, unit, StampedBoundLongMap::new);
	}
	
	public static <V extends ExpiringEntry> Cache<V> createCache(int capacity, long expireTime, long pollInterval, TimeUnit unit, IntFunction<LongMap<V>> mapFunc) {
		Cache<V> cache = new Cache<>(capacity, expireTime, pollInterval, unit, mapFunc);
		CACHES.add(cache);
		return cache;
	}
	
	public static void clear() {
		for(Cache<?> cache : CACHES) {
			cache.close();
		}
		
		CACHES.clear();
	}
}