package org.haberno.terraloomed.data.preset;

import net.minecraft.util.function.ToFloatFunction;
import net.minecraft.util.math.Spline;

// this is only for reference
@Deprecated
class VanillaTerrainProvider {

    public static <C, I extends ToFloatFunction<C>> Spline<C, I> overworldFactor(I continentalness, I erosion, I weirdness, I peaksAndValleys) {
        return Spline.builder(continentalness)
        	.add(-0.19F, 3.95F)
        	.add(-0.15F, getErosionFactor(erosion, weirdness, peaksAndValleys, 6.25F, true))
        	.add(-0.1F, getErosionFactor(erosion, weirdness, peaksAndValleys, 5.47F, true))
        	.add(0.03F, getErosionFactor(erosion, weirdness, peaksAndValleys, 5.08F, true))
        	.add(0.06F, getErosionFactor(erosion, weirdness, peaksAndValleys, 4.69F, false))
        	.build();
    }

    private static <C, I extends ToFloatFunction<C>> Spline<C, I> getErosionFactor(I erosion, I weirdness, I peaksAndValleys, float cavesFactor, boolean hasRivers) {
        Spline<C, I> cubicSpline = Spline.builder(weirdness)
        	.add(-0.2F, 6.3F)
        	.add(0.2F, cavesFactor)
        	.build();
        Spline.Builder<C, I> erosionSpline = Spline.builder(erosion)
        	.add(-0.6F, cubicSpline)
        	.add(-0.5F, Spline.builder(weirdness)
        		.add(-0.05F, 6.3F)
        		.add(0.05F, 2.67F)
        		.build()
        	)
        	.add(-0.35F, cubicSpline)
        	.add(-0.25F, cubicSpline)
        	.add(-0.1F, Spline.builder(weirdness)
        		.add(-0.05F, 2.67F)
        		.add(0.05F, 6.3F)
        		.build()
        	)
        	.add(0.03F, cubicSpline);
        if (hasRivers) {
            Spline<C, I> cubicSpline2 = Spline.builder(weirdness)
            	.add(0.0F, cavesFactor)
            	.add(0.1F, 0.625F)
            	.build();
            Spline<C, I> cubicSpline3 = Spline.builder(peaksAndValleys)
            	.add(-0.9F, cavesFactor)
            	.add(-0.69F, cubicSpline2)
            	.build();
            erosionSpline.add(0.35F, cavesFactor)
            	.add(0.45F, cubicSpline3)
            	.add(0.55F, cubicSpline3)
            	.add(0.62F, cavesFactor);
        } else {
            Spline<C, I> cubicSpline2 = Spline.builder(peaksAndValleys)
            	.add(-0.7F, cubicSpline)
            	.add(-0.15F, 1.37F)
            	.build();
            Spline<C, I> cubicSpline3 = Spline.builder(peaksAndValleys)
            	.add(0.45F, cubicSpline)
            	.add(0.7F, 1.56F)
            	.build();
            erosionSpline
            	.add(0.05F, cubicSpline3)
            	.add(0.4F, cubicSpline3)
            	.add(0.45F, cubicSpline2)
            	.add(0.55F, cubicSpline2)
            	.add(0.58F, cavesFactor);
        }
        return erosionSpline.build();
    }
}