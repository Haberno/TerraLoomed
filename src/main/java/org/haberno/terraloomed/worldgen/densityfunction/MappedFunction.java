package org.haberno.terraloomed.worldgen.densityfunction;

import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public interface MappedFunction extends DensityFunction.Base {

	@Override
	default CodecHolder<? extends Marker> getCodecHolder() {
		throw new UnsupportedOperationException();
	}
	
	public interface Marker extends Base {

		@Override
		default double sample(NoisePos ctx) {
			throw new UnsupportedOperationException();
		}
			
		@Override
		default double minValue() {
			return Double.NEGATIVE_INFINITY;
		}

		@Override
		default double maxValue() {
			return Double.POSITIVE_INFINITY;
		}
	}
}
