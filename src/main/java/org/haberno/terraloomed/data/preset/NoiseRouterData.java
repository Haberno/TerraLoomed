package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.noise.InterpolatedNoiseSampler;
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.OreVeinSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.NoiseRouter;

import java.util.stream.Stream;

public class NoiseRouterData {
    public static final float GLOBAL_OFFSET = -0.50375f;
    private static final float ORE_THICKNESS = 0.08f;
    private static final double VEININESS_FREQUENCY = 1.5;
    private static final double NOODLE_SPACING_AND_STRAIGHTNESS = 1.5;
    private static final double SURFACE_DENSITY_THRESHOLD = 1.5625;
    private static final double CHEESE_NOISE_TARGET = -0.703125;
    public static final int ISLAND_CHUNK_DISTANCE = 64;
    public static final long ISLAND_CHUNK_DISTANCE_SQR = 4096L;
    private static final DensityFunction BLENDING_FACTOR = DensityFunctionTypes.constant(10.0);
    private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctionTypes.zero();
    private static final RegistryKey<DensityFunction> ZERO = NoiseRouterData.createKey("zero");
    public static final RegistryKey<DensityFunction> Y = NoiseRouterData.createKey("y");
    private static final RegistryKey<DensityFunction> SHIFT_X = NoiseRouterData.createKey("shift_x");
    private static final RegistryKey<DensityFunction> SHIFT_Z = NoiseRouterData.createKey("shift_z");
    public static final RegistryKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = NoiseRouterData.createKey("overworld/base_3d_noise");
    private static final RegistryKey<DensityFunction> BASE_3D_NOISE_NETHER = NoiseRouterData.createKey("nether/base_3d_noise");
    private static final RegistryKey<DensityFunction> BASE_3D_NOISE_END = NoiseRouterData.createKey("end/base_3d_noise");
    public static final RegistryKey<DensityFunction> CONTINENTS = NoiseRouterData.createKey("overworld/continents");
    public static final RegistryKey<DensityFunction> EROSION = NoiseRouterData.createKey("overworld/erosion");
    public static final RegistryKey<DensityFunction> RIDGES = NoiseRouterData.createKey("overworld/ridges");
    public static final RegistryKey<DensityFunction> RIDGES_FOLDED = NoiseRouterData.createKey("overworld/ridges_folded");
    public static final RegistryKey<DensityFunction> OFFSET = NoiseRouterData.createKey("overworld/offset");
    public static final RegistryKey<DensityFunction> FACTOR = NoiseRouterData.createKey("overworld/factor");
    public static final RegistryKey<DensityFunction> JAGGEDNESS = NoiseRouterData.createKey("overworld/jaggedness");
    public static final RegistryKey<DensityFunction> DEPTH = NoiseRouterData.createKey("overworld/depth");
    public static final RegistryKey<DensityFunction> SLOPED_CHEESE = NoiseRouterData.createKey("overworld/sloped_cheese");
    public static final RegistryKey<DensityFunction> CONTINENTS_LARGE = NoiseRouterData.createKey("overworld_large_biomes/continents");
    public static final RegistryKey<DensityFunction> EROSION_LARGE = NoiseRouterData.createKey("overworld_large_biomes/erosion");
    private static final RegistryKey<DensityFunction> OFFSET_LARGE = NoiseRouterData.createKey("overworld_large_biomes/offset");
    private static final RegistryKey<DensityFunction> FACTOR_LARGE = NoiseRouterData.createKey("overworld_large_biomes/factor");
    private static final RegistryKey<DensityFunction> JAGGEDNESS_LARGE = NoiseRouterData.createKey("overworld_large_biomes/jaggedness");
    private static final RegistryKey<DensityFunction> DEPTH_LARGE = NoiseRouterData.createKey("overworld_large_biomes/depth");
    private static final RegistryKey<DensityFunction> SLOPED_CHEESE_LARGE = NoiseRouterData.createKey("overworld_large_biomes/sloped_cheese");
    private static final RegistryKey<DensityFunction> OFFSET_AMPLIFIED = NoiseRouterData.createKey("overworld_amplified/offset");
    private static final RegistryKey<DensityFunction> FACTOR_AMPLIFIED = NoiseRouterData.createKey("overworld_amplified/factor");
    private static final RegistryKey<DensityFunction> JAGGEDNESS_AMPLIFIED = NoiseRouterData.createKey("overworld_amplified/jaggedness");
    private static final RegistryKey<DensityFunction> DEPTH_AMPLIFIED = NoiseRouterData.createKey("overworld_amplified/depth");
    private static final RegistryKey<DensityFunction> SLOPED_CHEESE_AMPLIFIED = NoiseRouterData.createKey("overworld_amplified/sloped_cheese");
    private static final RegistryKey<DensityFunction> SLOPED_CHEESE_END = NoiseRouterData.createKey("end/sloped_cheese");
    public static final RegistryKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = NoiseRouterData.createKey("overworld/caves/spaghetti_roughness_function");
    public static final RegistryKey<DensityFunction> ENTRANCES = NoiseRouterData.createKey("overworld/caves/entrances");
    public static final RegistryKey<DensityFunction> NOODLE = NoiseRouterData.createKey("overworld/caves/noodle");
    public static final RegistryKey<DensityFunction> PILLARS = NoiseRouterData.createKey("overworld/caves/pillars");
    public static final RegistryKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = NoiseRouterData.createKey("overworld/caves/spaghetti_2d_thickness_modulator");
    public static final RegistryKey<DensityFunction> SPAGHETTI_2D = NoiseRouterData.createKey("overworld/caves/spaghetti_2d");

    private static RegistryKey<DensityFunction> createKey(String string) {
        return RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, new Identifier(string));
    }

    public static RegistryEntry<? extends DensityFunction> bootstrap(Registerable<DensityFunction> bootstapContext) {
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter = bootstapContext.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS);
        RegistryEntryLookup<DensityFunction> holderGetter2 = bootstapContext.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION);
        bootstapContext.register(ZERO, DensityFunctionTypes.zero());
        int i = DimensionType.MIN_HEIGHT * 2;
        int j = DimensionType.MAX_COLUMN_HEIGHT * 2;
        bootstapContext.register(Y, DensityFunctionTypes.yClampedGradient(i, j, i, j));
        DensityFunction densityFunction = NoiseRouterData.registerAndWrap(bootstapContext, SHIFT_X, DensityFunctionTypes.flatCache(DensityFunctionTypes.cache2d(DensityFunctionTypes.shiftA(holderGetter.getOrThrow(NoiseParametersKeys.OFFSET)))));
        DensityFunction densityFunction2 = NoiseRouterData.registerAndWrap(bootstapContext, SHIFT_Z, DensityFunctionTypes.flatCache(DensityFunctionTypes.cache2d(DensityFunctionTypes.shiftB(holderGetter.getOrThrow(NoiseParametersKeys.OFFSET)))));
        bootstapContext.register(BASE_3D_NOISE_OVERWORLD, InterpolatedNoiseSampler.createBase3dNoiseFunction(0.25, 0.125, 80.0, 160.0, 8.0));
        bootstapContext.register(BASE_3D_NOISE_NETHER, InterpolatedNoiseSampler.createBase3dNoiseFunction(0.25, 0.375, 80.0, 60.0, 8.0));
        bootstapContext.register(BASE_3D_NOISE_END, InterpolatedNoiseSampler.createBase3dNoiseFunction(0.25, 0.25, 80.0, 160.0, 4.0));
        RegistryEntry.Reference<DensityFunction> holder = bootstapContext.register(CONTINENTS, DensityFunctionTypes.flatCache(DensityFunctionTypes.shiftedNoise(densityFunction, densityFunction2, 0.25, holderGetter.getOrThrow(NoiseParametersKeys.CONTINENTALNESS))));
        RegistryEntry.Reference<DensityFunction> holder2 = bootstapContext.register(EROSION, DensityFunctionTypes.flatCache(DensityFunctionTypes.shiftedNoise(densityFunction, densityFunction2, 0.25, holderGetter.getOrThrow(NoiseParametersKeys.EROSION))));
        DensityFunction densityFunction3 = NoiseRouterData.registerAndWrap(bootstapContext, RIDGES, DensityFunctionTypes.flatCache(DensityFunctionTypes.shiftedNoise(densityFunction, densityFunction2, 0.25, holderGetter.getOrThrow(NoiseParametersKeys.RIDGE))));
        bootstapContext.register(RIDGES_FOLDED, NoiseRouterData.peaksAndValleys(densityFunction3));
        DensityFunction densityFunction4 = DensityFunctionTypes.noise(holderGetter.getOrThrow(NoiseParametersKeys.JAGGED), 1500.0, 0.0);
        NoiseRouterData.registerTerrainNoises(bootstapContext, holderGetter2, densityFunction4, holder, holder2, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE, false);
        RegistryEntry.Reference<DensityFunction> holder3 = bootstapContext.register(CONTINENTS_LARGE, DensityFunctionTypes.flatCache(DensityFunctionTypes.shiftedNoise(densityFunction, densityFunction2, 0.25, holderGetter.getOrThrow(NoiseParametersKeys.CONTINENTALNESS_LARGE))));
        RegistryEntry.Reference<DensityFunction> holder4 = bootstapContext.register(EROSION_LARGE, DensityFunctionTypes.flatCache(DensityFunctionTypes.shiftedNoise(densityFunction, densityFunction2, 0.25, holderGetter.getOrThrow(NoiseParametersKeys.EROSION_LARGE))));
        NoiseRouterData.registerTerrainNoises(bootstapContext, holderGetter2, densityFunction4, holder3, holder4, OFFSET_LARGE, FACTOR_LARGE, JAGGEDNESS_LARGE, DEPTH_LARGE, SLOPED_CHEESE_LARGE, false);
        NoiseRouterData.registerTerrainNoises(bootstapContext, holderGetter2, densityFunction4, holder, holder2, OFFSET_AMPLIFIED, FACTOR_AMPLIFIED, JAGGEDNESS_AMPLIFIED, DEPTH_AMPLIFIED, SLOPED_CHEESE_AMPLIFIED, true);
        bootstapContext.register(SLOPED_CHEESE_END, DensityFunctionTypes.add(DensityFunctionTypes.endIslands(0L), NoiseRouterData.getFunction(holderGetter2, BASE_3D_NOISE_END)));
        bootstapContext.register(SPAGHETTI_ROUGHNESS_FUNCTION, NoiseRouterData.spaghettiRoughnessFunction(holderGetter));
        bootstapContext.register(SPAGHETTI_2D_THICKNESS_MODULATOR, DensityFunctionTypes.cacheOnce(DensityFunctionTypes.noiseInRange(holderGetter.getOrThrow(NoiseParametersKeys.SPAGHETTI_2D_THICKNESS), 2.0, 1.0, -0.6, -1.3)));
        bootstapContext.register(SPAGHETTI_2D, NoiseRouterData.spaghetti2D(holderGetter2, holderGetter));
        bootstapContext.register(ENTRANCES, NoiseRouterData.entrances(holderGetter2, holderGetter));
        bootstapContext.register(NOODLE, NoiseRouterData.noodle(holderGetter2, holderGetter));
        return bootstapContext.register(PILLARS, NoiseRouterData.pillars(holderGetter));
    }

    private static void registerTerrainNoises(Registerable<DensityFunction> bootstapContext, RegistryEntryLookup<DensityFunction> holderGetter, DensityFunction densityFunction, RegistryEntry<DensityFunction> holder, RegistryEntry<DensityFunction> holder2, RegistryKey<DensityFunction> resourceKey, RegistryKey<DensityFunction> resourceKey2, RegistryKey<DensityFunction> resourceKey3, RegistryKey<DensityFunction> resourceKey4, RegistryKey<DensityFunction> resourceKey5, boolean bl) {
        DensityFunctionTypes.Spline.DensityFunctionWrapper coordinate = new DensityFunctionTypes.Spline.DensityFunctionWrapper(holder);
        DensityFunctionTypes.Spline.DensityFunctionWrapper coordinate2 = new DensityFunctionTypes.Spline.DensityFunctionWrapper(holder2);
        DensityFunctionTypes.Spline.DensityFunctionWrapper coordinate3 = new DensityFunctionTypes.Spline.DensityFunctionWrapper(holderGetter.getOrThrow(RIDGES));
        DensityFunctionTypes.Spline.DensityFunctionWrapper coordinate4 = new DensityFunctionTypes.Spline.DensityFunctionWrapper(holderGetter.getOrThrow(RIDGES_FOLDED));
        DensityFunction densityFunction2 = NoiseRouterData.registerAndWrap(bootstapContext, resourceKey, NoiseRouterData.splineWithBlending(DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.50375f), DensityFunctionTypes.spline(VanillaTerrainParametersCreator.createOffsetSpline(coordinate, coordinate2, coordinate4, bl))), DensityFunctionTypes.blendOffset()));
        DensityFunction densityFunction3 = NoiseRouterData.registerAndWrap(bootstapContext, resourceKey2, NoiseRouterData.splineWithBlending(DensityFunctionTypes.spline(VanillaTerrainParametersCreator.createFactorSpline(coordinate, coordinate2, coordinate3, coordinate4, bl)), BLENDING_FACTOR));
        DensityFunction densityFunction4 = NoiseRouterData.registerAndWrap(bootstapContext, resourceKey4, DensityFunctionTypes.add(DensityFunctionTypes.yClampedGradient(-64, 320, 1.5, -1.5), densityFunction2));
        DensityFunction densityFunction5 = NoiseRouterData.registerAndWrap(bootstapContext, resourceKey3, NoiseRouterData.splineWithBlending(DensityFunctionTypes.spline(VanillaTerrainParametersCreator.createJaggednessSpline(coordinate, coordinate2, coordinate3, coordinate4, bl)), BLENDING_JAGGEDNESS));
        DensityFunction densityFunction6 = DensityFunctionTypes.mul(densityFunction5, densityFunction.halfNegative());
        DensityFunction densityFunction7 = NoiseRouterData.noiseGradientDensity(densityFunction3, DensityFunctionTypes.add(densityFunction4, densityFunction6));
        bootstapContext.register(resourceKey5, DensityFunctionTypes.add(densityFunction7, NoiseRouterData.getFunction(holderGetter, BASE_3D_NOISE_OVERWORLD)));
    }

    public static DensityFunction registerAndWrap(Registerable<DensityFunction> bootstapContext, RegistryKey<DensityFunction> resourceKey, DensityFunction densityFunction) {
        return new DensityFunctionTypes.RegistryEntryHolder(bootstapContext.register(resourceKey, densityFunction));
    }

    public static DensityFunction getFunction(RegistryEntryLookup<DensityFunction> holderGetter, RegistryKey<DensityFunction> resourceKey) {
        return new DensityFunctionTypes.RegistryEntryHolder(holderGetter.getOrThrow(resourceKey));
    }

    private static DensityFunction peaksAndValleys(DensityFunction densityFunction) {
        return DensityFunctionTypes.mul(DensityFunctionTypes.add(DensityFunctionTypes.add(densityFunction.abs(), DensityFunctionTypes.constant(-0.6666666666666666)).abs(), DensityFunctionTypes.constant(-0.3333333333333333)), DensityFunctionTypes.constant(-3.0));
    }

    public static float peaksAndValleys(float f) {
        return -(Math.abs(Math.abs(f) - 0.6666667f) - 0.33333334f) * 3.0f;
    }

    private static DensityFunction spaghettiRoughnessFunction(RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter) {
        DensityFunction densityFunction = DensityFunctionTypes.noise(holderGetter.getOrThrow(NoiseParametersKeys.SPAGHETTI_ROUGHNESS));
        DensityFunction densityFunction2 = DensityFunctionTypes.noiseInRange(holderGetter.getOrThrow(NoiseParametersKeys.SPAGHETTI_ROUGHNESS_MODULATOR), 0.0, -0.1);
        return DensityFunctionTypes.cacheOnce(DensityFunctionTypes.mul(densityFunction2, DensityFunctionTypes.add(densityFunction.abs(), DensityFunctionTypes.constant(-0.4))));
    }

    public static DensityFunction entrances(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2) {
        DensityFunction densityFunction = DensityFunctionTypes.cacheOnce(DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_RARITY), 2.0, 1.0));
        DensityFunction densityFunction2 = DensityFunctionTypes.noiseInRange(holderGetter2.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_THICKNESS), -0.065, -0.088);
        DensityFunction densityFunction3 = DensityFunctionTypes.weirdScaledSampler(densityFunction, holderGetter2.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_1), DensityFunctionTypes.WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction densityFunction4 = DensityFunctionTypes.weirdScaledSampler(densityFunction, holderGetter2.getOrThrow(NoiseParametersKeys.SPAGHETTI_3D_2), DensityFunctionTypes.WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction densityFunction5 = DensityFunctionTypes.add(DensityFunctionTypes.max(densityFunction3, densityFunction4), densityFunction2).clamp(-1.0, 1.0);
        DensityFunction densityFunction6 = NoiseRouterData.getFunction(holderGetter, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction densityFunction7 = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.CAVE_ENTRANCE), 0.75, 0.5);
        DensityFunction densityFunction8 = DensityFunctionTypes.add(DensityFunctionTypes.add(densityFunction7, DensityFunctionTypes.constant(0.37)), DensityFunctionTypes.yClampedGradient(-10, 30, 0.3, 0.0));
        return DensityFunctionTypes.cacheOnce(DensityFunctionTypes.min(densityFunction8, DensityFunctionTypes.add(densityFunction6, densityFunction5)));
    }

    private static DensityFunction noodle(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2) {
        DensityFunction densityFunction = NoiseRouterData.getFunction(holderGetter, Y);
        int i = -64;
        int j = -60;
        int k = 320;
        DensityFunction densityFunction2 = NoiseRouterData.yLimitedInterpolatable(densityFunction, DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.NOODLE), 1.0, 1.0), -60, 320, -1);
        DensityFunction densityFunction3 = NoiseRouterData.yLimitedInterpolatable(densityFunction, DensityFunctionTypes.noiseInRange(holderGetter2.getOrThrow(NoiseParametersKeys.NOODLE_THICKNESS), 1.0, 1.0, -0.05, -0.1), -60, 320, 0);
        double d = 2.6666666666666665;
        DensityFunction densityFunction4 = NoiseRouterData.yLimitedInterpolatable(densityFunction, DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.NOODLE_RIDGE_A), 2.6666666666666665, 2.6666666666666665), -60, 320, 0);
        DensityFunction densityFunction5 = NoiseRouterData.yLimitedInterpolatable(densityFunction, DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.NOODLE_RIDGE_B), 2.6666666666666665, 2.6666666666666665), -60, 320, 0);
        DensityFunction densityFunction6 = DensityFunctionTypes.mul(DensityFunctionTypes.constant(1.5), DensityFunctionTypes.max(densityFunction4.abs(), densityFunction5.abs()));
        return DensityFunctionTypes.rangeChoice(densityFunction2, -1000000.0, 0.0, DensityFunctionTypes.constant(64.0), DensityFunctionTypes.add(densityFunction3, densityFunction6));
    }

    private static DensityFunction pillars(RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter) {
        double d = 25.0;
        double e = 0.3;
        DensityFunction densityFunction = DensityFunctionTypes.noise(holderGetter.getOrThrow(NoiseParametersKeys.PILLAR), 25.0, 0.3);
        DensityFunction densityFunction2 = DensityFunctionTypes.noiseInRange(holderGetter.getOrThrow(NoiseParametersKeys.PILLAR_RARENESS), 0.0, -2.0);
        DensityFunction densityFunction3 = DensityFunctionTypes.noiseInRange(holderGetter.getOrThrow(NoiseParametersKeys.PILLAR_THICKNESS), 0.0, 1.1);
        DensityFunction densityFunction4 = DensityFunctionTypes.add(DensityFunctionTypes.mul(densityFunction, DensityFunctionTypes.constant(2.0)), densityFunction2);
        return DensityFunctionTypes.cacheOnce(DensityFunctionTypes.mul(densityFunction4, densityFunction3.cube()));
    }

    private static DensityFunction spaghetti2D(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2) {
        DensityFunction densityFunction = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.SPAGHETTI_2D_MODULATOR), 2.0, 1.0);
        DensityFunction densityFunction2 = DensityFunctionTypes.weirdScaledSampler(densityFunction, holderGetter2.getOrThrow(NoiseParametersKeys.SPAGHETTI_2D), DensityFunctionTypes.WeirdScaledSampler.RarityValueMapper.TYPE2);
        DensityFunction densityFunction3 = DensityFunctionTypes.noiseInRange(holderGetter2.getOrThrow(NoiseParametersKeys.SPAGHETTI_2D_ELEVATION), 0.0, Math.floorDiv(-64, 8), 8.0);
        DensityFunction densityFunction4 = NoiseRouterData.getFunction(holderGetter, SPAGHETTI_2D_THICKNESS_MODULATOR);
        DensityFunction densityFunction5 = DensityFunctionTypes.add(densityFunction3, DensityFunctionTypes.yClampedGradient(-64, 320, 8.0, -40.0)).abs();
        DensityFunction densityFunction6 = DensityFunctionTypes.add(densityFunction5, densityFunction4).cube();
        double d = 0.083;
        DensityFunction densityFunction7 = DensityFunctionTypes.add(densityFunction2, DensityFunctionTypes.mul(DensityFunctionTypes.constant(0.083), densityFunction4));
        return DensityFunctionTypes.max(densityFunction7, densityFunction6).clamp(-1.0, 1.0);
    }

    public static DensityFunction underground(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2, DensityFunction densityFunction) {
        DensityFunction densityFunction2 = NoiseRouterData.getFunction(holderGetter, SPAGHETTI_2D);
        DensityFunction densityFunction3 = NoiseRouterData.getFunction(holderGetter, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction densityFunction4 = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.CAVE_LAYER), 8.0);
        DensityFunction densityFunction5 = DensityFunctionTypes.mul(DensityFunctionTypes.constant(4.0), densityFunction4.square());
        DensityFunction densityFunction6 = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.CAVE_CHEESE), 0.6666666666666666);
        DensityFunction densityFunction7 = DensityFunctionTypes.add(DensityFunctionTypes.add(DensityFunctionTypes.constant(0.27), densityFunction6).clamp(-1.0, 1.0), DensityFunctionTypes.add(DensityFunctionTypes.constant(1.5), DensityFunctionTypes.mul(DensityFunctionTypes.constant(-0.64), densityFunction)).clamp(0.0, 0.5));
        DensityFunction densityFunction8 = DensityFunctionTypes.add(densityFunction5, densityFunction7);
        DensityFunction densityFunction9 = DensityFunctionTypes.min(DensityFunctionTypes.min(densityFunction8, NoiseRouterData.getFunction(holderGetter, ENTRANCES)), DensityFunctionTypes.add(densityFunction2, densityFunction3));
        DensityFunction densityFunction10 = NoiseRouterData.getFunction(holderGetter, PILLARS);
        DensityFunction densityFunction11 = DensityFunctionTypes.rangeChoice(densityFunction10, -1000000.0, 0.03, DensityFunctionTypes.constant(-1000000.0), densityFunction10);
        return DensityFunctionTypes.max(densityFunction9, densityFunction11);
    }

    public static DensityFunction postProcess(DensityFunction densityFunction) {
        DensityFunction densityFunction2 = DensityFunctionTypes.blendDensity(densityFunction);
        return DensityFunctionTypes.mul(DensityFunctionTypes.interpolated(densityFunction2), DensityFunctionTypes.constant(0.64)).squeeze();
    }

    protected static NoiseRouter overworld(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2, boolean bl, boolean bl2) {
        DensityFunction densityFunction = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.AQUIFER_BARRIER), 0.5);
        DensityFunction densityFunction2 = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction densityFunction3 = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction densityFunction4 = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.AQUIFER_LAVA));
        DensityFunction densityFunction5 = NoiseRouterData.getFunction(holderGetter, SHIFT_X);
        DensityFunction densityFunction6 = NoiseRouterData.getFunction(holderGetter, SHIFT_Z);
        DensityFunction densityFunction7 = DensityFunctionTypes.shiftedNoise(densityFunction5, densityFunction6, 0.25, holderGetter2.getOrThrow(bl ? NoiseParametersKeys.TEMPERATURE_LARGE : NoiseParametersKeys.TEMPERATURE));
        DensityFunction densityFunction8 = DensityFunctionTypes.shiftedNoise(densityFunction5, densityFunction6, 0.25, holderGetter2.getOrThrow(bl ? NoiseParametersKeys.VEGETATION_LARGE : NoiseParametersKeys.VEGETATION));
        DensityFunction densityFunction9 = NoiseRouterData.getFunction(holderGetter, bl ? FACTOR_LARGE : (bl2 ? FACTOR_AMPLIFIED : FACTOR));
        DensityFunction densityFunction10 = NoiseRouterData.getFunction(holderGetter, bl ? DEPTH_LARGE : (bl2 ? DEPTH_AMPLIFIED : DEPTH));
        DensityFunction densityFunction11 = NoiseRouterData.noiseGradientDensity(DensityFunctionTypes.cache2d(densityFunction9), densityFunction10);
        DensityFunction densityFunction12 = NoiseRouterData.getFunction(holderGetter, bl ? SLOPED_CHEESE_LARGE : (bl2 ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE));
        DensityFunction densityFunction13 = DensityFunctionTypes.min(densityFunction12, DensityFunctionTypes.mul(DensityFunctionTypes.constant(5.0), NoiseRouterData.getFunction(holderGetter, ENTRANCES)));
        DensityFunction densityFunction14 = DensityFunctionTypes.rangeChoice(densityFunction12, -1000000.0, 1.5625, densityFunction13, NoiseRouterData.underground(holderGetter, holderGetter2, densityFunction12));
        DensityFunction densityFunction15 = DensityFunctionTypes.min(NoiseRouterData.postProcess(NoiseRouterData.slideOverworld(bl2, densityFunction14)), NoiseRouterData.getFunction(holderGetter, NOODLE));
        DensityFunction densityFunction16 = NoiseRouterData.getFunction(holderGetter, Y);
        int i = Stream.of(OreVeinSampler.VeinType.values()).mapToInt(veinType -> veinType.minY).min().orElse(-DimensionType.MIN_HEIGHT * 2);
        int j = Stream.of(OreVeinSampler.VeinType.values()).mapToInt(veinType -> veinType.maxY).max().orElse(-DimensionType.MIN_HEIGHT * 2);
        DensityFunction densityFunction17 = NoiseRouterData.yLimitedInterpolatable(densityFunction16, DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.ORE_VEININESS), 1.5, 1.5), i, j, 0);
        float f = 4.0f;
        DensityFunction densityFunction18 = NoiseRouterData.yLimitedInterpolatable(densityFunction16, DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.ORE_VEIN_A), 4.0, 4.0), i, j, 0).abs();
        DensityFunction densityFunction19 = NoiseRouterData.yLimitedInterpolatable(densityFunction16, DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.ORE_VEIN_B), 4.0, 4.0), i, j, 0).abs();
        DensityFunction densityFunction20 = DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.08f), DensityFunctionTypes.max(densityFunction18, densityFunction19));
        DensityFunction densityFunction21 = DensityFunctionTypes.noise(holderGetter2.getOrThrow(NoiseParametersKeys.ORE_GAP));
        return new NoiseRouter(densityFunction, densityFunction2, densityFunction3, densityFunction4, densityFunction7, densityFunction8, NoiseRouterData.getFunction(holderGetter, bl ? CONTINENTS_LARGE : CONTINENTS), NoiseRouterData.getFunction(holderGetter, bl ? EROSION_LARGE : EROSION), densityFunction10, NoiseRouterData.getFunction(holderGetter, RIDGES), NoiseRouterData.slideOverworld(bl2, DensityFunctionTypes.add(densityFunction11, DensityFunctionTypes.constant(-0.703125)).clamp(-64.0, 64.0)), densityFunction15, densityFunction17, densityFunction20, densityFunction21);
    }

    private static NoiseRouter noNewCaves(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2, DensityFunction densityFunction) {
        DensityFunction densityFunction2 = NoiseRouterData.getFunction(holderGetter, SHIFT_X);
        DensityFunction densityFunction3 = NoiseRouterData.getFunction(holderGetter, SHIFT_Z);
        DensityFunction densityFunction4 = DensityFunctionTypes.shiftedNoise(densityFunction2, densityFunction3, 0.25, holderGetter2.getOrThrow(NoiseParametersKeys.TEMPERATURE));
        DensityFunction densityFunction5 = DensityFunctionTypes.shiftedNoise(densityFunction2, densityFunction3, 0.25, holderGetter2.getOrThrow(NoiseParametersKeys.VEGETATION));
        DensityFunction densityFunction6 = NoiseRouterData.postProcess(densityFunction);
        return new NoiseRouter(DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), densityFunction4, densityFunction5, DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), densityFunction6, DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero());
    }

    private static DensityFunction slideOverworld(boolean bl, DensityFunction densityFunction) {
        return NoiseRouterData.slide(densityFunction, -64, 384, bl ? 16 : 80, bl ? 0 : 64, -0.078125, 0, 24, bl ? 0.4 : 0.1171875);
    }

    private static DensityFunction slideNetherLike(RegistryEntryLookup<DensityFunction> holderGetter, int i, int j) {
        return NoiseRouterData.slide(NoiseRouterData.getFunction(holderGetter, BASE_3D_NOISE_NETHER), i, j, 24, 0, 0.9375, -8, 24, 2.5);
    }

    private static DensityFunction slideEndLike(DensityFunction densityFunction, int i, int j) {
        return NoiseRouterData.slide(densityFunction, i, j, 72, -184, -23.4375, 4, 32, -0.234375);
    }

    protected static NoiseRouter nether(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2) {
        return NoiseRouterData.noNewCaves(holderGetter, holderGetter2, NoiseRouterData.slideNetherLike(holderGetter, 0, 128));
    }

    protected static NoiseRouter caves(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2) {
        return NoiseRouterData.noNewCaves(holderGetter, holderGetter2, NoiseRouterData.slideNetherLike(holderGetter, -64, 192));
    }

    protected static NoiseRouter floatingIslands(RegistryEntryLookup<DensityFunction> holderGetter, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> holderGetter2) {
        return NoiseRouterData.noNewCaves(holderGetter, holderGetter2, NoiseRouterData.slideEndLike(NoiseRouterData.getFunction(holderGetter, BASE_3D_NOISE_END), 0, 256));
    }

    private static DensityFunction slideEnd(DensityFunction densityFunction) {
        return NoiseRouterData.slideEndLike(densityFunction, 0, 128);
    }

    protected static NoiseRouter end(RegistryEntryLookup<DensityFunction> holderGetter) {
        DensityFunction densityFunction = DensityFunctionTypes.cache2d(DensityFunctionTypes.endIslands(0L));
        DensityFunction densityFunction2 = NoiseRouterData.postProcess(NoiseRouterData.slideEnd(NoiseRouterData.getFunction(holderGetter, SLOPED_CHEESE_END)));
        return new NoiseRouter(DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), densityFunction, DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), NoiseRouterData.slideEnd(DensityFunctionTypes.add(densityFunction, DensityFunctionTypes.constant(-0.703125))), densityFunction2, DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero());
    }

    protected static NoiseRouter none() {
        return new NoiseRouter(DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero(), DensityFunctionTypes.zero());
    }

    private static DensityFunction splineWithBlending(DensityFunction densityFunction, DensityFunction densityFunction2) {
        DensityFunction densityFunction3 = DensityFunctionTypes.lerp(DensityFunctionTypes.blendAlpha(), densityFunction2, densityFunction);
        return DensityFunctionTypes.flatCache(DensityFunctionTypes.cache2d(densityFunction3));
    }

    public static DensityFunction noiseGradientDensity(DensityFunction densityFunction, DensityFunction densityFunction2) {
        DensityFunction densityFunction3 = DensityFunctionTypes.mul(densityFunction2, densityFunction);
        return DensityFunctionTypes.mul(DensityFunctionTypes.constant(4.0), densityFunction3.quarterNegative());
    }

    public static DensityFunction yLimitedInterpolatable(DensityFunction densityFunction, DensityFunction densityFunction2, int i, int j, int k) {
        return DensityFunctionTypes.interpolated(DensityFunctionTypes.rangeChoice(densityFunction, i, j + 1, densityFunction2, DensityFunctionTypes.constant(k)));
    }

    private static DensityFunction slide(DensityFunction densityFunction, int i, int j, int k, int l, double d, int m, int n, double e) {
        DensityFunction densityFunction2 = densityFunction;
        DensityFunction densityFunction3 = DensityFunctionTypes.yClampedGradient(i + j - k, i + j - l, 1.0, 0.0);
        densityFunction2 = DensityFunctionTypes.lerp(densityFunction3, d, densityFunction2);
        DensityFunction densityFunction4 = DensityFunctionTypes.yClampedGradient(i + m, i + n, 0.0, 1.0);
        densityFunction2 = DensityFunctionTypes.lerp(densityFunction4, e, densityFunction2);
        return densityFunction2;
    }

    protected static final class QuantizedSpaghettiRarity {
        protected QuantizedSpaghettiRarity() {
        }

        protected static double getSphaghettiRarity2D(double d) {
            if (d < -0.75) {
                return 0.5;
            }
            if (d < -0.5) {
                return 0.75;
            }
            if (d < 0.5) {
                return 1.0;
            }
            if (d < 0.75) {
                return 2.0;
            }
            return 3.0;
        }

        protected static double getSpaghettiRarity3D(double d) {
            if (d < -0.5) {
                return 0.75;
            }
            if (d < 0.0) {
                return 1.0;
            }
            if (d < 0.5) {
                return 1.5;
            }
            return 2.0;
        }
    }
}

