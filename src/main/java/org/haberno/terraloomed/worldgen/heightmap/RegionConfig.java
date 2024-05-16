package org.haberno.terraloomed.worldgen.heightmap;

import raccoonman.reterraforged.world.worldgen.noise.module.Noise;

public record RegionConfig(int seed, int scale, Noise warpX, Noise warpZ, float warpStrength) {
}
