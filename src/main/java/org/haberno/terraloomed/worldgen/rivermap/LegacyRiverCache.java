package org.haberno.terraloomed.worldgen.rivermap;


import org.haberno.terraloomed.worldgen.noise.NoiseUtil;

public class LegacyRiverCache extends RiverCache {
	
    public LegacyRiverCache(RiverGenerator generator) {
        super(generator);
    }
    
    @Override
    public Rivermap getRivers(int x, int z) {
        return this.cache.computeIfAbsent(NoiseUtil.seed(x, z), id -> this.generator.generateRivers(x, z, id));
    }
}
