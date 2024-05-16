package org.haberno.terraloomed.worldgen.densityfunction;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import org.haberno.terraloomed.worldgen.noise.NoiseUtil;

import java.util.ArrayList;
import java.util.List;

public record LinearSplineFunction(DensityFunction input, List<Pair<Double, DensityFunction>> points, double minValue, double maxValue) implements DensityFunction {
	public static final Codec<LinearSplineFunction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(LinearSplineFunction::input),
		Codecs.nonEmptyList(Codec.pair(Codec.DOUBLE, DensityFunction.FUNCTION_CODEC).listOf()).fieldOf("points").forGetter(LinearSplineFunction::points)
	).apply(instance, LinearSplineFunction::new));
	
	public LinearSplineFunction(DensityFunction input, List<Pair<Double, DensityFunction>> points) {
		this(input, points, min(points), max(points));
	}
	
	@Override
	public double sample(NoisePos ctx) {
		double input = this.input.sample(ctx);
		int pointCount = this.points.size();
		
		Pair<Double, DensityFunction> first = this.points.get(0);
		if(input <= first.getFirst()) {
			return first.getSecond().sample(ctx);
		}

		Pair<Double, DensityFunction> last = this.points.get(pointCount - 1);
		if(input >= last.getFirst()) {
			return last.getSecond().sample(ctx);
		}
		
		int index = MathHelper.binarySearch(0, pointCount, i -> input < this.points.get(i).getFirst()) - 1;
		Pair<Double, DensityFunction> start = this.points.get(index);
		Pair<Double, DensityFunction> end = this.points.get(index + 1);
		double min = start.getFirst();
		double max = end.getFirst();
		double from = start.getSecond().sample(ctx);
		double to = end.getSecond().sample(ctx);
		
		double lerp = NoiseUtil.map(input, 0.0D, 1.0D, min, max);
		lerp = NoiseUtil.clamp(lerp, 0.0D, 1.0D);
		return NoiseUtil.lerp(from, to, lerp);
	}

	@Override
	public void fill(double[] array, EachApplier ctxProvider) {
		ctxProvider.fill(array, this);
	}

	@Override
	public DensityFunction apply(DensityFunctionVisitor visitor) {
		return visitor.apply(new LinearSplineFunction(this.input.apply(visitor), this.points.stream().map((point) -> {
			return Pair.of(point.getFirst(), visitor.apply(point.getSecond()));
		}).toList()));
	}

	@Override
	public CodecHolder<LinearSplineFunction> getCodecHolder() {
		return new CodecHolder<>(CODEC);
	}

	public static Builder builder(DensityFunction input) {
		return new Builder(input);
	}
	
	private static float min(List<Pair<Double, DensityFunction>> points) {
		return (float) points.stream().map(Pair::getSecond).mapToDouble(DensityFunction::minValue).min().orElseThrow();
	}
	
	private static float max(List<Pair<Double, DensityFunction>> points) {
		return (float) points.stream().map(Pair::getSecond).mapToDouble(DensityFunction::maxValue).max().orElseThrow();
	}
	
	public static class Builder {
		private DensityFunction input;
		private List<Pair<Double, DensityFunction>> points;
		
		public Builder(DensityFunction input) {
			this.input = input;
			this.points = new ArrayList<>();
		}
		
		public Builder addPoint(double point, double value) {
			return this.addPoint(point, DensityFunctionTypes.constant(value));
		}

		public Builder addPoint(double point, DensityFunction value) {
			this.points.add(Pair.of(point, value));
			return this;
		}
		
		public LinearSplineFunction build() {
			return new LinearSplineFunction(this.input, ImmutableList.copyOf(this.points));
		}
	}
}
