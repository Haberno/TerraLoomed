package org.haberno.terraloomed.worldgen.cell;


import org.haberno.terraloomed.worldgen.noise.module.Noise;

public interface CellPopulator {
    void apply(Cell cell, float x, float z);
    
    default CellPopulator mapNoise(Noise.Visitor visitor) {
    	return this;
    }
}
