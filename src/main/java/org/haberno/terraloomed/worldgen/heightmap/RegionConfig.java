package org.haberno.terraloomed.worldgen.heightmap;


import org.haberno.terraloomed.worldgen.noise.module.Noise;

public record RegionConfig(int seed, int scale, Noise warpX, Noise warpZ, float warpStrength) {
}
