package org.haberno.terraloomed.worldgen.biome.spawn;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseValuePoint;
import org.haberno.terraloomed.worldgen.biome.RTFClimateSampler;

import java.util.List;

// TODO replace this with a mixin once we can get that working
public class SpawnFinderFix {
	public Result result;

	public SpawnFinderFix(List<NoiseHypercube> list, MultiNoiseSampler sampler) {
		if ((Object) sampler instanceof RTFClimateSampler rtfClimateSampler) {
			BlockPos center = rtfClimateSampler.getSpawnSearchCenter();

			this.result = SpawnFinderFix.getSpawnPositionAndFitness(list, sampler, center.getX(), center.getZ());
			this.radialSearch(list, sampler, 2048.0f, 512.0f);
			this.radialSearch(list, sampler, 512.0f, 32.0f);
		}
	}

	private void radialSearch(List<NoiseHypercube> list, MultiNoiseSampler sampler, float f, float g) {
		float h = 0.0f;
		float i = g;
		BlockPos blockPos = this.result.location();
		while (i <= f) {
			int j = blockPos.getX() + (int) (Math.sin(h) * (double) i);
			Result result = SpawnFinderFix.getSpawnPositionAndFitness(list, sampler, j,
					blockPos.getZ() + (int) (Math.cos(h) * (double) i));
			if (result.fitness() < this.result.fitness()) {
				this.result = result;
			}
			if (!((double) (h += g / i) > Math.PI * 2))
				continue;
			h = 0.0f;
			i += g;
		}
	}

	private static Result getSpawnPositionAndFitness(List<NoiseHypercube> list, MultiNoiseSampler sampler, int i, int j) {
		double d = MathHelper.square(2500.0);
		long l = (long) ((double) MathHelper.square(10000.0f)
				* Math.pow((double) (MathHelper.square((long) i) + MathHelper.square((long) j)) / d, 2.0));
		NoiseValuePoint targetPoint = sampler.sample(BiomeCoords.fromBlock(i), 0, BiomeCoords.fromBlock(j));
		NoiseValuePoint targetPoint2 = new NoiseValuePoint(targetPoint.temperatureNoise(), targetPoint.humidityNoise(),
				targetPoint.continentalnessNoise(), targetPoint.erosionNoise(), 0L, targetPoint.weirdnessNoise());
		long m = Long.MAX_VALUE;
		for (NoiseHypercube parameterPoint : list) {
			m = Math.min(m, parameterPoint.getSquaredDistance(targetPoint2));
		}
		return new Result(new BlockPos(i, 0, j), l + m);
	}

	public record Result(BlockPos location, long fitness) {
	}
}
