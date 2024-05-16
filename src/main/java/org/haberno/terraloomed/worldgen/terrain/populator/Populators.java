package org.haberno.terraloomed.worldgen.terrain.populator;

import raccoonman.reterraforged.data.preset.settings.TerrainSettings;
import raccoonman.reterraforged.world.worldgen.biome.Erosion;
import raccoonman.reterraforged.world.worldgen.biome.Weirdness;
import raccoonman.reterraforged.world.worldgen.cell.CellField;
import raccoonman.reterraforged.world.worldgen.heightmap.Levels;
import raccoonman.reterraforged.world.worldgen.heightmap.RegionConfig;
import raccoonman.reterraforged.world.worldgen.noise.domain.Domain;
import raccoonman.reterraforged.world.worldgen.noise.domain.Domains;
import raccoonman.reterraforged.world.worldgen.noise.function.CellFunction;
import raccoonman.reterraforged.world.worldgen.noise.function.DistanceFunction;
import raccoonman.reterraforged.world.worldgen.noise.function.EdgeFunction;
import raccoonman.reterraforged.world.worldgen.noise.function.Interpolation;
import raccoonman.reterraforged.world.worldgen.noise.module.Erosion.BlendMode;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;
import raccoonman.reterraforged.world.worldgen.terrain.Terrain;
import raccoonman.reterraforged.world.worldgen.terrain.TerrainType;
import raccoonman.reterraforged.world.worldgen.util.Seed;

public class Populators {
	private static final float PEAK = 0.7666667F;
	
	public static OceanPopulator makeDeepOcean(@Deprecated int seed, float seaLevel) {
		Noise hills = Noises.perlin(++seed, 150, 3);
		hills = Noises.mul(hills, seaLevel * 0.7F);

		Noise hillBias = Noises.perlin(++seed, 200, 1);
		hillBias = Noises.mul(hillBias, seaLevel * 0.2F);
		
		hills = Noises.add(hills, hillBias);
		
		Noise canyons = Noises.perlin(++seed, 150, 4);
		canyons = Noises.powCurve(canyons, 0.2F);
		canyons = Noises.invert(canyons);
		canyons = Noises.mul(canyons, seaLevel * 0.7F);
		
		Noise canyonBias = Noises.perlin(++seed, 170, 1);
		canyonBias = Noises.mul(canyonBias, seaLevel * 0.15F);
		
		canyons = Noises.add(canyons, canyonBias);
		
		Noise selector = Noises.perlin(++seed, 500, 1);
		
		Noise height = Noises.blend(selector, hills, canyons, 0.6F, 0.65F);
		height = Noises.warpPerlin(height, ++seed, 50, 2, 50.0F);
		return new OceanPopulator(TerrainType.DEEP_OCEAN, height);
	}
    
	public static OceanPopulator makeShallowOcean(Levels levels) {
		 return new OceanPopulator(TerrainType.SHALLOW_OCEAN, Noises.constant(levels.water(-7)));
	}
	
	public static OceanPopulator makeCoast(Levels levels) {
		return new OceanPopulator(TerrainType.COAST, Noises.constant(levels.water));
	}
	
    public static TerrainPopulator makeSteppe(Seed seed, Noise ground, TerrainSettings.Terrain settings) {
        int scaleH = Math.round(250.0F * settings.horizontalScale);

        Noise base = Noises.perlin(seed.next(), scaleH * 2, 3, 3.75F);
        base = Noises.alpha(base, 0.45F);
        
        Noise warpX = Noises.perlin(seed.next(), scaleH / 4, 3, 3.0F);
        Noise warpZ = Noises.perlin(seed.next(), scaleH / 4, 3, 3.0F);
        
        Noise weirdness = Noises.perlin(seed.next(), scaleH, 1);
        weirdness = Noises.mul(weirdness, base);
        weirdness = Noises.warp(weirdness, warpX, warpZ, scaleH / 4.0F);
        weirdness = Noises.warpPerlin(weirdness, seed.next(), 256, 1, 200.0F);
        weirdness = Noises.cache2d(weirdness);
        
        Noise height = Noises.mul(weirdness, 0.08F);
        height = Noises.add(height, -0.02F);
		return TerrainPopulator.make(TerrainType.STEPPE, ground, height, Noises.constant(0.425F), Noises.add(Noises.mul(weirdness, 0.15F), 0.055F), settings);
    }
    
    private static TerrainPopulator makePlains(Seed seed, Noise ground, TerrainSettings.Terrain noiseSettings, TerrainSettings.Terrain scalingSettings, float verticalScale, Terrain terrainType) {
    	int scaleH = Math.round(250.0F * noiseSettings.horizontalScale);
      	
		Noise base = Noises.perlin(seed.next(), scaleH * 2, 3, 3.75F);
      	base = Noises.alpha(base, 0.45F);
      	
      	Noise warpX = Noises.perlin(seed.next(), scaleH / 4, 3, 3.5F);
      	Noise warpZ = Noises.perlin(seed.next(), scaleH / 4, 3, 3.5F);
      	
      	Noise weirdness = Noises.perlin(seed.next(), scaleH, 1);
      	weirdness = Noises.mul(weirdness, base);
      	weirdness = Noises.warp(weirdness, warpX, warpZ, scaleH / 4.0F);
      	weirdness = Noises.warpPerlin(weirdness, seed.next(), 256, 1, 256.0F);
        weirdness = Noises.cache2d(weirdness);
        
      	Noise height = Noises.mul(weirdness, 0.15F * verticalScale);
      	height = Noises.add(height, -0.02F);
      	return TerrainPopulator.make(terrainType, ground, height, Noises.constant(0.35F), Noises.add(Noises.mul(weirdness, 0.15F), 0.055F), scalingSettings);
    }

    public static TerrainPopulator makePlains(Seed seed, Noise ground, TerrainSettings.Terrain settings, float verticalScale) {
    	return makePlains(seed, ground, settings, settings, verticalScale, TerrainType.PLAINS);
    }
	
	public static TerrainPopulator makePlateau(Seed seed, Noise ground, TerrainSettings.Terrain settings, float verticalScale) {
		Noise valley = Noises.perlinRidge(seed.next(), 500, 1);
		
		valley = Noises.invert(valley);
		valley = Noises.warpPerlin(valley, seed.next(), 100, 1, 150.0F);
		valley = Noises.warpPerlin(valley, seed.next(), 20, 1, 15.0F);
				
		Noise top = Noises.perlinRidge(seed.next(), 150, 3, 2.45F);
		top = Noises.warpPerlin(top, seed.next(), 300, 1, 150.0F);
		top = Noises.warpPerlin(top, seed.next(), 40, 2, 20.0F);
		top = Noises.mul(top, 0.15F);
		
		Noise valleyScaler = Noises.clamp(valley, 0.02F, 0.1F);
		valleyScaler = Noises.map(valleyScaler, 0.0F, 1.0F);
		
		top = Noises.mul(top, valleyScaler);
		
		Noise surface = Noises.perlin(seed.next(), 25, 2);
		surface = Noises.mul(surface, 0.0275F);
		surface = Noises.warpPerlin(surface, seed.next(), 40, 2, 20.0F);
		
		Noise cubic = Noises.cubic(seed.next(), 500, 1);
		cubic = Noises.mul(cubic, 0.6F);
		cubic = Noises.add(cubic, 0.3F);
		
		Noise valleyBase = Noises.mul(valley, cubic);
		valleyBase = Noises.add(valleyBase, top);
		valleyBase = Noises.mul(valleyBase, terrainMask());
		
		Noise weirdness = Noises.terrace(valleyBase, Noises.constant(0.9F), Noises.constant(0.15F), Noises.constant(0.35F), 0.1F, 4);
		weirdness = Noises.add(weirdness, surface);
		weirdness = Noises.cache2d(weirdness);
		
		Noise height = Noises.mul(weirdness, 0.475F * verticalScale);
		return TerrainPopulator.make(TerrainType.PLATEAU, ground, height, Erosion.LEVEL_2.source(), Noises.add(Noises.mul(weirdness, 0.5F), 0.15F), settings);
	}
	
	public static TerrainPopulator makeHills1(Seed seed, Noise ground, TerrainSettings.Terrain settings, float verticalScale) {
		Noise weirdness = Noises.perlin(seed.next(), 200, 3);
		
		Noise scaler = Noises.billow(seed.next(), 400, 3);
		scaler = Noises.alpha(scaler, 0.5F);
		
		weirdness = Noises.mul(weirdness, scaler);
		weirdness = Noises.warpPerlin(weirdness, seed.next(), 30, 3, 20.0F);
		weirdness = Noises.warpPerlin(weirdness, seed.next(), 400, 3, 200.0F);
		weirdness = Noises.cache2d(weirdness);
		
		Noise height = Noises.mul(weirdness, 0.6F * verticalScale);
		return TerrainPopulator.make(TerrainType.HILLS_1, ground, height, Erosion.LEVEL_4.source(), Noises.add(weirdness, 0.1F), settings);
	}

	public static TerrainPopulator makeHills2(Seed seed, Noise ground, TerrainSettings.Terrain settings, float verticalScale) {
		Noise weirdness = Noises.cubic(seed.next(), 128, 2);

		Noise scaler1 = Noises.perlin(seed.next(), 32, 4);
		scaler1 = Noises.alpha(scaler1, 0.075F);
		weirdness = Noises.mul(weirdness, scaler1);
		
		weirdness = Noises.warpPerlin(weirdness, seed.next(), 30, 3, 20.0F);
		weirdness = Noises.warpPerlin(weirdness, seed.next(), 400, 3, 200.0F);

		Noise scaler2 = Noises.perlinRidge(seed.next(), 512, 2);
		scaler2 = Noises.alpha(scaler2, 0.8F);
		weirdness = Noises.mul(weirdness, scaler2);
		weirdness = Noises.cache2d(weirdness);
		
		Noise height = Noises.mul(weirdness, 0.55F * verticalScale);
		return TerrainPopulator.make(TerrainType.HILLS_2, ground, height, Erosion.LEVEL_4.source(), weirdness, settings);
	}

	public static TerrainPopulator makeDales(Seed seed, Noise ground, TerrainSettings.Terrain settings) {
		Noise hills1 = Noises.billow(seed.next(), 300, 4, 4.0F, 0.8F);
		hills1 = Noises.powCurve(hills1, 0.5F);
		hills1 = Noises.mul(hills1, 0.75F);
		
		Noise hills2 = Noises.billow(seed.next(), 350, 3, 4.0F, 0.8F);
		hills2 = Noises.pow(hills2, 1.25F);
		
		Noise selector = Noises.perlin(seed.next(), 400, 1);
		selector = Noises.clamp(selector, 0.3F, 0.6F);
		selector = Noises.map(selector, 0.0F, 1.0F);
		selector = Noises.cache2d(selector);
		
		int warpSeed = seed.next();
		
		Noise hillsBlend = Noises.blend(selector, hills1, hills2, 0.4F, 0.75F);
		
		Noise height = hillsBlend;
		height = Noises.pow(height, 1.125F);
		height = Noises.warpPerlin(height, warpSeed, 300, 1, 100.0F);
		
		Noise weirdness = Noises.blend(selector, hills1, hills2, 0.4F, 0.75F);
		weirdness = Noises.warpPerlin(weirdness, warpSeed, 300, 1, 100.0F);
		
		Noise erosion = Noises.threshold(Noises.warpPerlin(selector, warpSeed, 300, 1, 100.0F), Noises.constant(Erosion.LEVEL_3.max() - 0.05F), Noises.constant(Erosion.LEVEL_4.min() + 0.05F), 0.2F);
		
		return TerrainPopulator.make(TerrainType.DALES, ground, Noises.mul(height, 0.4F), erosion, Noises.add(Noises.mul(weirdness, 0.7F), 0.1F), settings);
	}

	public static TerrainPopulator makeBadlands(Seed seed, Noise ground, TerrainSettings.Terrain settings) {
		Noise mask = Noises.perlin(seed.next(), 270, 3);
		mask = Noises.clamp(mask, 0.35F, 0.65F);
		mask = Noises.map(mask, 0.0F, 1.0F);
		
		Noise hills = Noises.perlinRidge(seed.next(), 275, 4);
		hills = Noises.warpPerlin(hills, seed.next(), 400, 2, 100.0F);
		hills = Noises.warpPerlin(hills, seed.next(), 18, 1, 20.0F);
		hills = Noises.mul(hills, mask);
		
		float modulation = 0.4F;
		float alpha = 1.0F - modulation;
		
		Noise mod1 = Noises.warpPerlin(hills, seed.next(), 100, 1, 50.0F);
		mod1 = Noises.mul(mod1, modulation);
		
		Noise lowFreq = Noises.steps(hills, 4, 0.6F, 0.7F);
		lowFreq = Noises.mul(lowFreq, alpha);
		lowFreq = Noises.add(lowFreq, mod1);
		
		Noise highFreq = Noises.steps(hills, 10, 0.6F, 0.7F);
		highFreq = Noises.mul(highFreq, alpha);
		highFreq = Noises.add(highFreq, mod1);
		
		Noise detail = Noises.add(lowFreq, highFreq);
		detail = Noises.alpha(detail, 0.5F);
		
		Noise scaler = Noises.perlin(seed.next(), 200, 3);
		scaler = Noises.mul(scaler, modulation);
		
		Noise mod2 = Noises.mul(hills, scaler);
		
		Noise shape = Noises.steps(hills, 4, 0.65F, 0.75F, Interpolation.CURVE3);
		shape = Noises.mul(shape, alpha);
		shape = Noises.add(shape, mod2);
		shape = Noises.mul(shape, alpha);
		
		Noise weirdness = Noises.mul(shape, detail);
		weirdness = Noises.cache2d(weirdness);
		
		Noise height = Noises.mul(weirdness, 0.55F);
		height = Noises.add(height, 0.025F);
		return TerrainPopulator.make(TerrainType.BADLANDS, ground, height, Erosion.LEVEL_3.source(), Noises.add(weirdness, 0.125F), settings);
	}
	
	// hills and plains should have separate erosion values
	public static TerrainPopulator makeTorridonian(Seed seed, Noise ground, TerrainSettings.Terrain settings) {
		Noise plains = Noises.perlin(seed.next(), 100, 3);
		plains = Noises.warpPerlin(plains, seed.next(), 300, 1, 150.0F);
		plains = Noises.warpPerlin(plains, seed.next(), 20, 1, 40.0F);
		plains = Noises.mul(plains, 0.15F);
		
		Noise hills = Noises.perlin(seed.next(), 150, 4);
		hills = Noises.warpPerlin(hills, seed.next(), 300, 1, 200.0F);
		hills = Noises.warpPerlin(hills, seed.next(), 20, 2, 20.0F);
		hills = Noises.boost(hills);
		
		Noise erosion = Noises.perlin(seed.next(), 200, 3);
		
		Noise modulation = Noises.perlin(seed.next(), 120, 1);
		modulation = Noises.mul(modulation, 0.25F);
		
		Noise mask = Noises.perlin(seed.next(), 200, 1);
		mask = Noises.mul(mask, 0.5F);
		mask = Noises.add(mask, 0.5F);
		
		Noise slope = Noises.constant(0.5F);
		
		Noise blend = Noises.blend(erosion, plains, hills, 0.6F, 0.6F);
		blend = Noises.cache2d(blend);
		
		Noise height = Noises.advancedTerrace(blend, modulation, mask, slope, 0.0F, 0.3F, 6, 1);
		height = Noises.boost(height);
		height = Noises.mul(height, 0.5F);

		return TerrainPopulator.make(TerrainType.TORRIDONIAN, ground, height, Noises.constant(0.54F), Noises.add(Noises.mul(blend, 0.575F), 0.35F), settings);
	}

	private static TerrainPopulator makeMountains(Terrain terrainType, Seed seed, Noise ground, TerrainSettings.Terrain terrainSettings, float verticalScale, boolean makeFancy) {
		int scaleH = Math.round(410.0F * terrainSettings.horizontalScale);
		
		Noise base = Noises.perlinRidge(seed.next(), scaleH, 4, 2.35F, 1.15F);
		
		Noise scaler = Noises.perlin(seed.next(), 24, 4);
		scaler = Noises.alpha(scaler, 0.075F);
		
		Noise weirdness = Noises.mul(base, scaler);
		int warpSeed = seed.next();
		weirdness = Noises.warpPerlin(weirdness, warpSeed, 350, 1, 150.0F);
		weirdness = Noises.mul(weirdness, terrainMask());

		if(makeFancy) {
			weirdness = makeFancy(seed, weirdness);
		}
		weirdness = Noises.cache2d(weirdness);
		return TerrainPopulator.make(terrainType, ground, Noises.mul(weirdness, 0.7F * verticalScale), Noises.constant(-1.0F), Noises.add(Noises.mul(Noises.min(weirdness, 1.0F), PEAK), 0.06F), terrainSettings);
	}

	public static TerrainPopulator makeMountains(Seed seed, Noise ground, TerrainSettings.Terrain terrainSettings, float verticalScale, boolean makeFancy) { 
		return makeMountains(TerrainType.MOUNTAINS_1, seed, ground, terrainSettings, verticalScale, makeFancy);
	}
	
	public static TerrainPopulator makeMountainChain(Seed seed, Noise ground, TerrainSettings.Terrain terrainSettings, float verticalScale, boolean makeFancy) { 
		return makeMountains(TerrainType.MOUNTAIN_CHAIN, seed, ground, terrainSettings, verticalScale, makeFancy);
	}
	
	public static TerrainPopulator makeMountains2(Seed seed, Noise ground, TerrainSettings.Terrain terrainSettings, float verticalScale, boolean makeFancy) {
		Noise cell = Noises.worleyEdge(seed.next(), Math.round(360 * terrainSettings.horizontalScale), EdgeFunction.DISTANCE_2, DistanceFunction.EUCLIDEAN);
		cell = Noises.mul(cell, 1.2F);
		cell = Noises.clamp(cell, 0.0F, 1.0F);
		cell = Noises.warpPerlin(cell, seed.next(), 200, 2, 100.0F);
		
		Noise blur = Noises.perlin(seed.next(), 10, 1);
		blur = Noises.alpha(blur, 0.025F);
		
		Noise surface = Noises.perlinRidge(seed.next(), 125, 4);
		surface = Noises.alpha(surface, 0.37F);
		
		Noise weirdness = Noises.clamp(cell, 0.0F, 1.0F);
		weirdness = Noises.mul(weirdness, blur);
		weirdness = Noises.mul(weirdness, surface);
		weirdness = Noises.mul(surface, terrainMask());
		weirdness = Noises.pow(weirdness, 1.1F);
		if(makeFancy) { 
			weirdness = makeFancy(seed, weirdness);
		}
		weirdness = Noises.cache2d(weirdness);
		return TerrainPopulator.make(TerrainType.MOUNTAINS_2, ground, Noises.mul(weirdness, 0.645F * verticalScale), Noises.constant(-1.0F), Noises.add(Noises.mul(Noises.min(weirdness, 1.0F), PEAK), 0.06F), terrainSettings);
	}
	
    public static TerrainPopulator makeMountains3(Seed seed, Noise ground, TerrainSettings.Terrain terrainSettings, float verticalScale, boolean makeFancy) {
    	Noise cell = Noises.worleyEdge(seed.next(), Math.round(400 * terrainSettings.horizontalScale), EdgeFunction.DISTANCE_2, DistanceFunction.EUCLIDEAN);
    	cell = Noises.mul(cell, 1.2F);
    	cell = Noises.clamp(cell, 0.0F, 1.0F);
    	cell = Noises.warpPerlin(cell, seed.next(), 200, 2, 100.0F);

    	Noise blur = Noises.perlin(seed.next(), 10, 1);
    	blur = Noises.alpha(blur, 0.025F);
    	
    	Noise surface = Noises.perlinRidge(seed.next(), 125, 4);
    	surface = Noises.alpha(surface, 0.37F);
    	
    	Noise weirdness = Noises.clamp(cell, 0.0F, 1.0F);
    	weirdness = Noises.mul(weirdness, blur);
    	weirdness = Noises.mul(weirdness, surface);
		weirdness = Noises.mul(surface, terrainMask());
		weirdness = Noises.pow(weirdness, 1.1F);
    	weirdness = Noises.cache2d(weirdness);
    	
    	Noise modulation = Noises.perlin(seed.next(), 50, 1);
    	modulation = Noises.mul(modulation, 0.5F);
    	
    	Noise mask = Noises.perlin(seed.next(), 100, 1);
    	mask = Noises.clamp(mask, 0.5F, 0.95F);
    	mask = Noises.map(mask, 0.0F, 1.0F);
    	
    	Noise slope = Noises.constant(0.45F);
    	
    	Noise height = Noises.advancedTerrace(weirdness, modulation, mask, slope, 0.20000000298023224F, 0.44999998807907104F, 24, 1);
    	
    	if(makeFancy) {
        	height = makeFancy(seed, height);
    	}
		return TerrainPopulator.make(TerrainType.MOUNTAINS_3, ground, Noises.mul(height, 0.645F * verticalScale), Noises.constant(-1.0F), Noises.add(Noises.mul(Noises.min(weirdness, 1.0F), PEAK), 0.06F), terrainSettings);
    }

    public static VolcanoPopulator makeVolcano(Seed seed, Noise ground, RegionConfig region, Levels levels, float weight) {
    	float midpoint = 0.3F;
        float range = 0.3F;
        Noise heightLookup = Noises.perlin(seed.next(), 2, 1);
        heightLookup = Noises.map(heightLookup, 0.45F, 0.65F);
        
        Noise height = Noises.worley(region.seed(), region.scale(), CellFunction.NOISE_LOOKUP, DistanceFunction.EUCLIDEAN, heightLookup);
        height = Noises.warp(height, region.warpX(), region.warpZ(), region.warpStrength());
        
        Noise cone = Noises.worleyEdge(region.seed(), region.scale(), EdgeFunction.DISTANCE_2_DIV, DistanceFunction.EUCLIDEAN);
        cone = Noises.invert(cone);
        cone = Noises.warp(cone, region.warpX(), region.warpZ(), region.warpStrength());
        cone = Noises.powCurve(cone, 11.0F);
        cone = Noises.clamp(cone, 0.475F, 1.0F);
        cone = Noises.map(cone, 0.0F, 1.0F);
        cone = Noises.gradient(cone, 0.0F, 0.5F, 0.5F);
        cone = Noises.warpPerlin(cone, seed.next(), 15, 2, 10.0F);
        cone = Noises.mul(cone, height);
        
        Noise lowlands = Noises.perlinRidge(seed.next(), 150, 3);
        lowlands = Noises.warpPerlin(lowlands, seed.next(), 30, 1, 30.0F);
        lowlands = Noises.mul(lowlands, 0.1F);
        
        float blendLower = midpoint - range / 2.0F;
        float blendUpper = blendLower + range;
        float blendRange = blendUpper - blendLower;
        return new VolcanoPopulator(weight, ground, cone, height, lowlands, 0.94F, blendLower, blendUpper, blendRange, levels);
    }
	
	public static TerrainPopulator makeBorder(Seed seed, Noise ground, TerrainSettings.Terrain plainsSettings, TerrainSettings.Terrain steppeSettings, float verticalScale) {
		return makePlains(seed, ground, plainsSettings, steppeSettings, verticalScale, TerrainType.BORDER);
	}
    
	private static Noise makeFancy(Seed seed, Noise input) {
		Domain domain = Domains.direction(
			Noises.perlin(seed.next(), 10, 1),
			Noises.constant(2.0F)
		);
		Noise erosion = Noises.erosion(input, seed.next(), 2, 0.65F, 128.0F, 0.15F, 3.1F, 0.8F, BlendMode.CONSTANT);
		erosion = Noises.warp(erosion, domain);
		return erosion;
	}
	
	private static Noise terrainMask() {
		Noise terrainMask = Noises.cell(CellField.TERRAIN_MASK);
		terrainMask = Noises.clamp(terrainMask, 0.0F, 1.0F);
		return Noises.one();// terrainMask;
	}
}
