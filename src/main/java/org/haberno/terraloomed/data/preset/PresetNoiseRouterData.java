package org.haberno.terraloomed.data.preset;

import java.util.stream.Stream;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.NoiseRouter;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.data.preset.settings.WorldSettings;
import raccoonman.reterraforged.world.worldgen.cell.CellField;
import raccoonman.reterraforged.world.worldgen.densityfunction.RTFDensityFunctions;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;

public class PresetNoiseRouterData {
	private static final float SCALER = 128.0F;
	private static final float UNIT = 1.0F / SCALER;
	
    public static void bootstrap(Preset preset, Registerable<DensityFunction> ctx) {
        RegistryEntryLookup<DensityFunction> densityFunctions = ctx.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION);
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParams = ctx.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS);
        
    	WorldSettings worldSettings = preset.world();
    	WorldSettings.Properties properties = worldSettings.properties;
    	
        int worldHeight = properties.worldHeight;
        int worldDepth = properties.worldDepth;
        
        ctx.register(DensityFunctions.CONTINENTS_OVERWORLD, RTFDensityFunctions.cell(CellField.CONTINENTALNESS));
        ctx.register(DensityFunctions.EROSION_OVERWORLD, RTFDensityFunctions.cell(CellField.EROSION));
        ctx.register(DensityFunctions.RIDGES_OVERWORLD, RTFDensityFunctions.cell(CellField.WEIRDNESS));
        
        DensityFunction offset = DensityFunctions.registerAndGetHolder(ctx, DensityFunctions.OFFSET_OVERWORLD, DensityFunctionTypes.add(DensityFunctionTypes.constant(DensityFunctions.GLOBAL_OFFSET - 0.5F), DensityFunctionTypes.mul(RTFDensityFunctions.clampToNearestUnit(RTFDensityFunctions.cell(CellField.HEIGHT), properties.terrainScaler()), DensityFunctionTypes.constant(2.0D))));
        ctx.register(DensityFunctions.DEPTH_OVERWORLD, DensityFunctionTypes.add(DensityFunctionTypes.yClampedGradient(-worldDepth, worldHeight, yGradientRange(-worldDepth), yGradientRange(worldHeight)), offset));
        ctx.register(DensityFunctions.BASE_3D_NOISE_OVERWORLD, DensityFunctionTypes.zero());
        ctx.register(DensityFunctions.JAGGEDNESS_OVERWORLD, jaggednessPerformanceHack());
        ctx.register(DensityFunctions.CAVES_NOODLE_OVERWORLD, noodle(-worldDepth, worldHeight, densityFunctions, noiseParams));
        ctx.register(DensityFunctions.CAVES_SPAGHETTI_2D_OVERWORLD, spaghetti2D(-worldDepth, worldHeight, densityFunctions, noiseParams));
    }

    protected static NoiseRouter overworld(Preset preset, RegistryEntryLookup<DensityFunction> densityFunctions, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParams, RegistryEntryLookup<Noise> noises) {
    	WorldSettings worldSettings = preset.world();
    	WorldSettings.Properties properties = worldSettings.properties;
    	int worldDepth = properties.worldDepth;
    	
    	DensityFunction aquiferBarrier = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.AQUIFER_BARRIER), 0.5);
        DensityFunction aquiferFluidLevelFloodedness = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction aquiferFluidLevelSpread = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction aquiferLava = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.AQUIFER_LAVA));
        DensityFunction temperature = RTFDensityFunctions.cell(CellField.TEMPERATURE);
        DensityFunction vegetation = RTFDensityFunctions.cell(CellField.MOISTURE);
        DensityFunction factor = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.FACTOR_OVERWORLD);
        DensityFunction depth = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.DEPTH_OVERWORLD);
        DensityFunction initialDensity = DensityFunctions.createInitialDensityFunction(DensityFunctionTypes.cache2d(factor), depth);
        DensityFunction slopedCheese = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.SLOPED_CHEESE_OVERWORLD);
        DensityFunction entrances = slopedCheese;/* caves.entranceCaveChance > 0.0F ? DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0D), DensityFunctions.interpolated(NoiseRouterData.getFunction(densityFunctions, NoiseRouterData.ENTRANCES)))) :*/
        DensityFunction slopedCheeseRange = DensityFunctionTypes.mul(DensityFunctionTypes.rangeChoice(slopedCheese, -1000000.0D, 1.5625D, entrances, DensityFunctionTypes.interpolated(slideOverworld(underground(densityFunctions, noiseParams, slopedCheese), -worldDepth))), DensityFunctionTypes.constant(0.64)).squeeze();
        DensityFunction finalDensity = DensityFunctionTypes.min(slopedCheeseRange, DensityFunctions.entryHolder(densityFunctions, DensityFunctions.CAVES_NOODLE_OVERWORLD));
        DensityFunction y = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.Y);
        int minY = Stream.of(OreVeinifier.VeinType.values()).mapToInt(veinType -> veinType.minY).min().orElse(-DimensionType.MIN_HEIGHT * 2);
        int maxY = Stream.of(OreVeinifier.VeinType.values()).mapToInt(veinType -> veinType.maxY).max().orElse(-DimensionType.MIN_HEIGHT * 2);
        DensityFunction oreVeininess = DensityFunctions.verticalRangeChoice(y, DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.ORE_VEININESS), 1.5, 1.5), minY, maxY, 0);
        DensityFunction oreVeinA = DensityFunctions.verticalRangeChoice(y, DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.ORE_VEIN_A), 4.0, 4.0), minY, maxY, 0).abs();
        DensityFunction oreVeinB = DensityFunctions.verticalRangeChoice(y, DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.ORE_VEIN_B), 4.0, 4.0), minY, maxY, 0).abs();
        DensityFunction oreVein = DensityFunctionTypes.add(DensityFunctionTypes.constant(-0.08F), DensityFunctionTypes.max(oreVeinA, oreVeinB));
        DensityFunction oreGap = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.ORE_GAP));
        return new NoiseRouter(aquiferBarrier, aquiferFluidLevelFloodedness, aquiferFluidLevelSpread, aquiferLava, temperature, vegetation, DensityFunctions.entryHolder(densityFunctions, DensityFunctions.CONTINENTS_OVERWORLD), DensityFunctions.entryHolder(densityFunctions, DensityFunctions.EROSION_OVERWORLD), depth, DensityFunctions.entryHolder(densityFunctions, DensityFunctions.RIDGES_OVERWORLD), slideOverworld(DensityFunctionTypes.add(initialDensity, DensityFunctionTypes.constant(UNIT * -90)).clamp(-64.0, 64.0), -worldDepth), finalDensity, oreVeininess, oreVein, oreGap);
	}

    private static DensityFunction underground(RegistryEntryLookup<DensityFunction> densityFunctions, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParams, DensityFunction slopedCheese) {
        DensityFunction spaghetti2d = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.CAVES_SPAGHETTI_2D_OVERWORLD);
        DensityFunction spaghettiRoughnessFunction = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.CAVES_SPAGHETTI_ROUGHNESS_FUNCTION_OVERWORLD);
        DensityFunction caveLayerNoise = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.CAVE_LAYER), 8.0);
        DensityFunction caveLayer = DensityFunctionTypes.mul(DensityFunctionTypes.constant(4.0), caveLayerNoise.square());
        DensityFunction caveCheese = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.CAVE_CHEESE), 0.6666666666666666);
        DensityFunction slopedCaves = DensityFunctionTypes.add(DensityFunctionTypes.add(DensityFunctionTypes.constant(0.27), caveCheese).clamp(-1.0, 1.0), DensityFunctionTypes.add(DensityFunctionTypes.constant(1.5), DensityFunctionTypes.mul(DensityFunctionTypes.constant(-0.64), slopedCheese)).clamp(0.0, 0.5));
        DensityFunction slopedCaveLayered = DensityFunctionTypes.add(caveLayer, slopedCaves);
        DensityFunction underground = DensityFunctionTypes.min(DensityFunctionTypes.min(slopedCaveLayered, DensityFunctions.entryHolder(densityFunctions, DensityFunctions.CAVES_ENTRANCES_OVERWORLD)), DensityFunctionTypes.add(spaghetti2d, spaghettiRoughnessFunction));
        DensityFunction pillars = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.CAVES_PILLARS_OVERWORLD);
        DensityFunction pillarRange = DensityFunctionTypes.rangeChoice(pillars, -1000000.0, 0.03, DensityFunctionTypes.constant(-1000000.0), pillars);
        return DensityFunctionTypes.max(underground, pillarRange);
    }

    private static DensityFunction spaghetti2D(int minY, int maxY, RegistryEntryLookup<DensityFunction> densityFunctions, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParams) {
        DensityFunction modulator = DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.SPAGHETTI_2D_MODULATOR), 2.0, 1.0);
        DensityFunction sampler = DensityFunctionTypes.weirdScaledSampler(modulator, noiseParams.getOrThrow(NoiseParametersKeys.SPAGHETTI_2D), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE2);
        DensityFunction elevation = DensityFunctionTypes.noiseInRange(noiseParams.getOrThrow(NoiseParametersKeys.SPAGHETTI_2D_ELEVATION), 0.0, Math.floorDiv(minY, 8), 8.0);
        DensityFunction thicknessModulator = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.CAVES_SPAGHETTI_2D_THICKNESS_MODULATOR_OVERWORLD);
        DensityFunction elevationGradient = DensityFunctionTypes.add(elevation, DensityFunctionTypes.yClampedGradient(minY, maxY, minY / -8.0D, maxY / -8.0D)).abs();
        DensityFunction normal = DensityFunctionTypes.add(elevationGradient, thicknessModulator).cube();
        DensityFunction weird = DensityFunctionTypes.add(sampler, DensityFunctionTypes.mul(DensityFunctionTypes.constant(0.083D), thicknessModulator));
        return DensityFunctionTypes.max(weird, normal).clamp(-1.0D, 1.0D);
    }

    private static DensityFunction noodle(int minY, int maxY, RegistryEntryLookup<DensityFunction> densityFunctions, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParams) {
    	int baseY = minY + 4;
        
    	DensityFunction y = DensityFunctions.entryHolder(densityFunctions, DensityFunctions.Y);
        DensityFunction selector = DensityFunctions.verticalRangeChoice(y, DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.NOODLE), 1.0, 1.0), baseY, maxY, -1);
        DensityFunction thickness = DensityFunctions.verticalRangeChoice(y, DensityFunctionTypes.noiseInRange(noiseParams.getOrThrow(NoiseParametersKeys.NOODLE_THICKNESS), 1.0, 1.0, -0.05, -0.1), baseY, maxY, 0);
        DensityFunction ridgeA = DensityFunctions.verticalRangeChoice(y, DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.NOODLE_RIDGE_A), 2.6666666666666665, 2.6666666666666665), baseY, maxY, 0);
        DensityFunction ridgeB = DensityFunctions.verticalRangeChoice(y, DensityFunctionTypes.noise(noiseParams.getOrThrow(NoiseParametersKeys.NOODLE_RIDGE_B), 2.6666666666666665, 2.6666666666666665), baseY, maxY, 0);
        DensityFunction ridge = DensityFunctionTypes.mul(DensityFunctionTypes.constant(1.5), DensityFunctionTypes.max(ridgeA.abs(), ridgeB.abs()));
        return DensityFunctionTypes.rangeChoice(selector, -1000000.0, 0.0D, DensityFunctionTypes.constant(64.0), DensityFunctionTypes.add(thickness, ridge));
    }
    
    private static DensityFunction slideOverworld(DensityFunction function, int minY) {
        return slide(function, minY, 0, 24, UNIT * 15);
    }
    
    private static DensityFunction slide(DensityFunction function, int minY, int bottomGradientStart, int bottomGradientEnd, double bottomGradientTarget) {
        DensityFunction bottomGradient = DensityFunctionTypes.yClampedGradient(minY + bottomGradientStart, minY + bottomGradientEnd, 0.0, 1.0);
        return DensityFunctionTypes.lerp(bottomGradient, bottomGradientTarget, function);
    }
    
    /* 
     * the multiply function doesnt sample the second input
     * if the first input is zero, however this optimization doesn't get
     * applied if either input is a Constant, so we cant just use DensityFunctions.zero()
     */
    private static DensityFunction jaggednessPerformanceHack() {
    	return DensityFunctionTypes.add(DensityFunctionTypes.zero(), DensityFunctionTypes.zero());
    }
    
    private static float yGradientRange(float range) {
    	return 1.0F + (-range / SCALER);
    }
}