package org.haberno.terraloomed.worldgen.noise.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import raccoonman.reterraforged.world.worldgen.noise.NoiseUtil;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise.Visitor;

public record LinearSpline(org.haberno.map.worldgen.noise.module.Noise input, List<Pair<Float, org.haberno.map.worldgen.noise.module.Noise>> points, float minValue, float maxValue) implements org.haberno.map.worldgen.noise.module.Noise {
	public static final Codec<LinearSpline> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		org.haberno.map.worldgen.noise.module.Noise.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(LinearSpline::input),
		Codecs.nonEmptyList(Codec.pair(Codec.FLOAT, org.haberno.map.worldgen.noise.module.Noise.HOLDER_HELPER_CODEC).listOf()).fieldOf("points").forGetter(LinearSpline::points)
	).apply(instance, LinearSpline::new));
	
	public LinearSpline(org.haberno.map.worldgen.noise.module.Noise input, List<Pair<Float, org.haberno.map.worldgen.noise.module.Noise>> points) {
		this(input, points, min(points), max(points));
	}
	
	@Override
	public float compute(float x, float z, int seed) {
		float input = this.input.compute(x, z, seed);
		
		int pointCount = this.points.size();
		Pair<Float, org.haberno.map.worldgen.noise.module.Noise> first = this.points.get(0);
		Pair<Float, org.haberno.map.worldgen.noise.module.Noise> last = this.points.get(pointCount - 1);
		
		if(input <= first.getFirst()) {
			return first.getSecond().compute(x, z, seed);
		}
		
		if(input >= last.getFirst()) {
			return last.getSecond().compute(x, z, seed);
		}
		
		int index = MathHelper.binarySearch(0, pointCount, i -> input < this.points.get(i).getFirst()) - 1;
		Pair<Float, org.haberno.map.worldgen.noise.module.Noise> start = this.points.get(index);
		Pair<Float, org.haberno.map.worldgen.noise.module.Noise> end = this.points.get(index + 1);
		float min = start.getFirst();
		float max = end.getFirst();
		float from = start.getSecond().compute(x, z, seed);
		float to = end.getSecond().compute(x, z, seed);
		
		float lerp = NoiseUtil.map(input, 0.0F, 1.0F, min, max);
		lerp = NoiseUtil.clamp(lerp, 0.0F, 1.0F);
		
		return NoiseUtil.lerp(from, to, lerp);
	}

	@Override
	public org.haberno.map.worldgen.noise.module.Noise mapAll(org.haberno.map.worldgen.noise.module.Noise.Visitor visitor) {
		return visitor.apply(new LinearSpline(this.input.mapAll(visitor), this.points.stream().map((point) -> {
			return Pair.of(point.getFirst(), visitor.apply(point.getSecond()));
		}).toList()));
	}

	@Override
	public Codec<LinearSpline> codec() {
		return CODEC;
	}

	public static Builder builder(org.haberno.map.worldgen.noise.module.Noise noise) {
		return new Builder(noise);
	}
	
	private static float min(List<Pair<Float, org.haberno.map.worldgen.noise.module.Noise>> points) {
		return (float) points.stream().map(Pair::getSecond).mapToDouble(org.haberno.map.worldgen.noise.module.Noise::minValue).min().orElseThrow();
	}
	
	private static float max(List<Pair<Float, org.haberno.map.worldgen.noise.module.Noise>> points) {
		return (float) points.stream().map(Pair::getSecond).mapToDouble(org.haberno.map.worldgen.noise.module.Noise::maxValue).max().orElseThrow();
	}
	
	public static class Builder {
		private org.haberno.map.worldgen.noise.module.Noise input;
		private List<Pair<Float, org.haberno.map.worldgen.noise.module.Noise>> points;
		
		public Builder(org.haberno.map.worldgen.noise.module.Noise input) {
			this.input = input;
			this.points = new ArrayList<>();
		}
		
		public Builder addPoint(float point, float value) {
			return this.addPoint(point, org.haberno.map.worldgen.noise.module.Noises.constant(value));
		}

		public Builder addPoint(float point, org.haberno.map.worldgen.noise.module.Noise value) {
			this.points.add(Pair.of(point, value));
			return this;
		}
		
		public LinearSpline build() {
			return new LinearSpline(this.input, ImmutableList.copyOf(this.points));
		}
	}
}
