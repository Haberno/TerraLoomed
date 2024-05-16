package org.haberno.terraloomed.worldgen.rivermap;


import org.haberno.terraloomed.concurrent.cache.Cache;
import org.haberno.terraloomed.concurrent.cache.CacheManager;
import org.haberno.terraloomed.concurrent.cache.map.StampedLongMap;
import org.haberno.terraloomed.worldgen.util.PosUtil;

import java.util.concurrent.TimeUnit;

public class RiverCache {
    protected RiverGenerator generator;
    protected Cache<Rivermap> cache;
    
    public RiverCache(RiverGenerator generator) {
        this.cache = CacheManager.createCache(32, 5L, 1L, TimeUnit.MINUTES, StampedLongMap::new);
        this.generator = generator;
    }
    
    public Rivermap getRivers(int x, int z) {
        return this.cache.computeIfAbsent(PosUtil.pack(x, z), id -> {
        	return this.generator.generateRivers(PosUtil.unpackLeft(id), PosUtil.unpackRight(id), id);
        });
    }
}
