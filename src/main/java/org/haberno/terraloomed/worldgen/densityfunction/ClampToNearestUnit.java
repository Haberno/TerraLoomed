package org.haberno.terraloomed.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

record ClampToNearestUnit(DensityFunction function, int resolution) implements DensityFunction {
	public static final Codec<ClampToNearestUnit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		DensityFunction.FUNCTION_CODEC.fieldOf("function").forGetter(ClampToNearestUnit::function),
		Codec.INT.fieldOf("resolution").forGetter(ClampToNearestUnit::resolution)
	).apply(instance, ClampToNearestUnit::new));
	
	@Override
	public double sample(NoisePos ctx) {
		return this.computeClamped(this.function.sample(ctx));
	}

	@Override
	public void fill(double[] arr, EachApplier ctx) {
		this.function.fill(arr, ctx);
		for(int i = 0; i < arr.length; i++) {
			arr[i] = this.computeClamped(arr[i]);
		}
	}

	@Override
	public DensityFunction apply(DensityFunctionVisitor visitor) {
		return visitor.apply(new ClampToNearestUnit(this.function.apply(visitor), this.resolution));
	}

	@Override
	public double minValue() {
		return this.computeClamped(this.function.minValue());
	}

	@Override
	public double maxValue() {
		return this.computeClamped(this.function.maxValue());
	}

	@Override
	public CodecHolder<ClampToNearestUnit> getCodecHolder() {
		return new CodecHolder<>(REGISTRY_ENTRY_CODEC);
	}
	
	private double computeClamped(double value) {
		float scaled = (int) (value * this.resolution) + 1;
		return (scaled / this.resolution);
	}
}
