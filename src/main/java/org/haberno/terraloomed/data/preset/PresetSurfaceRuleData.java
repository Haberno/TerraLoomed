package org.haberno.terraloomed.data.preset;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.haberno.terraloomed.RTFCommon;
import org.haberno.terraloomed.data.preset.settings.MiscellaneousSettings;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.data.preset.settings.SurfaceSettings;
import org.haberno.terraloomed.data.preset.settings.WorldSettings;
import org.haberno.terraloomed.tags.RTFBlockTags;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.surface.condition.RTFSurfaceConditions;
import org.haberno.terraloomed.worldgen.surface.rule.RTFSurfaceRules;
import org.haberno.terraloomed.worldgen.surface.rule.StrataRule;
import org.haberno.terraloomed.worldgen.util.Scaling;

import java.util.ArrayList;
import java.util.List;

public class PresetSurfaceRuleData {
    private static final MaterialRules.MaterialRule AIR = PresetSurfaceRuleData.makeStateRule(Blocks.AIR);
    private static final MaterialRules.MaterialRule BEDROCK = PresetSurfaceRuleData.makeStateRule(Blocks.BEDROCK);
    private static final MaterialRules.MaterialRule WHITE_TERRACOTTA = PresetSurfaceRuleData.makeStateRule(Blocks.WHITE_TERRACOTTA);
    private static final MaterialRules.MaterialRule ORANGE_TERRACOTTA = PresetSurfaceRuleData.makeStateRule(Blocks.ORANGE_TERRACOTTA);
    private static final MaterialRules.MaterialRule BROWN_TERRACOTTA = PresetSurfaceRuleData.makeStateRule(Blocks.BROWN_TERRACOTTA);
    private static final MaterialRules.MaterialRule TERRACOTTA = PresetSurfaceRuleData.makeStateRule(Blocks.TERRACOTTA);
    private static final MaterialRules.MaterialRule RED_SAND = PresetSurfaceRuleData.makeStateRule(Blocks.RED_SAND);
    private static final MaterialRules.MaterialRule RED_SANDSTONE = PresetSurfaceRuleData.makeStateRule(Blocks.RED_SANDSTONE);
    private static final MaterialRules.MaterialRule STONE = PresetSurfaceRuleData.makeStateRule(Blocks.STONE);
    private static final MaterialRules.MaterialRule DEEPSLATE = PresetSurfaceRuleData.makeStateRule(Blocks.DEEPSLATE);
    private static final MaterialRules.MaterialRule DIRT = PresetSurfaceRuleData.makeStateRule(Blocks.DIRT);
    private static final MaterialRules.MaterialRule PODZOL = PresetSurfaceRuleData.makeStateRule(Blocks.PODZOL);
    private static final MaterialRules.MaterialRule COARSE_DIRT = PresetSurfaceRuleData.makeStateRule(Blocks.COARSE_DIRT);
    private static final MaterialRules.MaterialRule MYCELIUM = PresetSurfaceRuleData.makeStateRule(Blocks.MYCELIUM);
    private static final MaterialRules.MaterialRule GRASS_BLOCK = PresetSurfaceRuleData.makeStateRule(Blocks.GRASS_BLOCK);
    private static final MaterialRules.MaterialRule CALCITE = PresetSurfaceRuleData.makeStateRule(Blocks.CALCITE);
    private static final MaterialRules.MaterialRule GRAVEL = PresetSurfaceRuleData.makeStateRule(Blocks.GRAVEL);
    private static final MaterialRules.MaterialRule SAND = PresetSurfaceRuleData.makeStateRule(Blocks.SAND);
    private static final MaterialRules.MaterialRule SANDSTONE = PresetSurfaceRuleData.makeStateRule(Blocks.SANDSTONE);
    private static final MaterialRules.MaterialRule SMOOTH_SANDSTONE = PresetSurfaceRuleData.makeStateRule(Blocks.SMOOTH_SANDSTONE);
    private static final MaterialRules.MaterialRule PACKED_ICE = PresetSurfaceRuleData.makeStateRule(Blocks.PACKED_ICE);
    private static final MaterialRules.MaterialRule SNOW_BLOCK = PresetSurfaceRuleData.makeStateRule(Blocks.SNOW_BLOCK);
    private static final MaterialRules.MaterialRule MUD = PresetSurfaceRuleData.makeStateRule(Blocks.MUD);
    private static final MaterialRules.MaterialRule POWDER_SNOW = PresetSurfaceRuleData.makeStateRule(Blocks.POWDER_SNOW);
    private static final MaterialRules.MaterialRule ICE = PresetSurfaceRuleData.makeStateRule(Blocks.ICE);
    private static final MaterialRules.MaterialRule WATER = PresetSurfaceRuleData.makeStateRule(Blocks.WATER);

    private static final Identifier STRATA_CACHE_ID = RTFCommon.location("default");
    
    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
    
    public static MaterialRules.MaterialRule overworld(Preset preset, RegistryEntryLookup<Noise> noise) {
		WorldSettings worldSettings = preset.world();
		WorldSettings.Properties properties = worldSettings.properties;
		Scaling scaling = Scaling.make(properties.terrainScaler(), properties.seaLevel);
    	MiscellaneousSettings miscellaneousSettings = preset.miscellaneous();
    	
    	SurfaceSettings surfaceSettings = preset.surface();
    	SurfaceSettings.Erosion erosion = surfaceSettings.erosion();

    	int strataBufferAmount = 5;
    	
    	MaterialRules.MaterialCondition y4BelowSurface = MaterialRules.stoneDepth(3, false, VerticalSurfaceType.FLOOR);
        MaterialRules.MaterialCondition below97 = MaterialRules.aboveY(YOffset.fixed(97), 2);
        MaterialRules.MaterialCondition below256 = MaterialRules.aboveY(YOffset.fixed(256), 0);
        MaterialRules.MaterialCondition above63 = MaterialRules.aboveYWithStoneDepth(YOffset.fixed(63), -1);
        MaterialRules.MaterialCondition above74 = MaterialRules.aboveYWithStoneDepth(YOffset.fixed(74), 1);
        MaterialRules.MaterialCondition below60 = MaterialRules.aboveY(YOffset.fixed(60), 0);
        MaterialRules.MaterialCondition below62 = MaterialRules.aboveY(YOffset.fixed(62), 0);
        MaterialRules.MaterialCondition below63 = MaterialRules.aboveY(YOffset.fixed(63), 0);
        MaterialRules.MaterialCondition y1BelowSurface = MaterialRules.water(-1, 0);
        MaterialRules.MaterialCondition yOnSurface = MaterialRules.water(0, 0);
        MaterialRules.MaterialCondition y6BelowSurface = MaterialRules.waterWithStoneDepth(-6, -1);
        MaterialRules.MaterialCondition hole = MaterialRules.hole();
        MaterialRules.MaterialCondition frozenOcean = MaterialRules.biome(BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN);
        MaterialRules.MaterialCondition badlands = MaterialRules.biome(BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS, BiomeKeys.WOODED_BADLANDS);
        MaterialRules.MaterialCondition steep = MaterialRules.steepSlope();
        MaterialRules.MaterialCondition erodedRock = RTFSurfaceConditions.steepness(erosion.rockSteepness, noise.getOrThrow(PresetSurfaceNoise.STEEPNESS_VARIANCE));
        MaterialRules.MaterialRule erodedDirt = makeErodedDirtRule(noise, erosion);
        MaterialRules.MaterialRule grass = MaterialRules.sequence(
        	erodedDirt,
        	MaterialRules.condition(
        		yOnSurface,
        		GRASS_BLOCK
        	), 
        	DIRT
        );
        MaterialRules.MaterialRule sand = MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, SANDSTONE), SAND);
        MaterialRules.MaterialRule gravel = MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, STONE), GRAVEL);
        MaterialRules.MaterialRule windswept = MaterialRules.sequence(
        	MaterialRules.condition(
        		RTFSurfaceConditions.sediment(5.3F), 
        		STONE
            ),
        	MaterialRules.condition(
        		RTFSurfaceConditions.sediment(2.4F), 
        		gravel
            ),
        	MaterialRules.condition(
        		RTFSurfaceConditions.erosion(8.25F),
        		STONE
        	),
        	MaterialRules.condition(
	        	RTFSurfaceConditions.erosion(5.5F),
	        	gravel
	        )
        );
        MaterialRules.MaterialCondition sandyBeach = MaterialRules.biome(BiomeKeys.WARM_OCEAN, BiomeKeys.BEACH, BiomeKeys.SNOWY_BEACH);
        MaterialRules.MaterialCondition desert = MaterialRules.biome(BiomeKeys.DESERT);
        MaterialRules.MaterialRule stony = MaterialRules.sequence(
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.STONY_PEAKS), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				MaterialRules.noiseThreshold(NoiseParametersKeys.CALCITE, -0.0125, 0.0125), 
        				CALCITE
        			), 
        			STONE
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.STONY_SHORE), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				MaterialRules.noiseThreshold(NoiseParametersKeys.GRAVEL, -0.05, 0.05), 
        				gravel
        			), 
        			STONE
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.WINDSWEPT_HILLS), 
        		MaterialRules.condition(
//        			PresetSurfaceRuleData.surfaceNoiseAbove(1.0), 
        			RTFSurfaceConditions.sediment(5.0F),
        			STONE
        		)
        	), 
        	MaterialRules.condition(
        		sandyBeach, 
        		sand
        	), 
        	MaterialRules.condition(
        		desert, 
        		sand
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.DRIPSTONE_CAVES), 
        		STONE
        	)
        );
        MaterialRules.MaterialRule snowUnderFloor = MaterialRules.condition(
        	MaterialRules.noiseThreshold(NoiseParametersKeys.POWDER_SNOW, 0.45, 0.58), 
        	MaterialRules.condition(
        		yOnSurface,
        		POWDER_SNOW
        	)
        );
        MaterialRules.MaterialRule snowOnFloor = MaterialRules.condition(
        	MaterialRules.noiseThreshold(NoiseParametersKeys.POWDER_SNOW, 0.35, 0.6), 
        	MaterialRules.condition(
        		yOnSurface, 
        		POWDER_SNOW
        	)
        );
        MaterialRules.MaterialRule underFloor = MaterialRules.sequence(
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.FROZEN_PEAKS), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				steep, 
        				PACKED_ICE
        			), 
        			MaterialRules.condition(
        				MaterialRules.noiseThreshold(NoiseParametersKeys.PACKED_ICE, -0.5, 0.2), 
        				PACKED_ICE
        			), 
        			MaterialRules.condition(
        				MaterialRules.noiseThreshold(NoiseParametersKeys.ICE, -0.0625, 0.025), 
        				ICE
        			), 
        			MaterialRules.condition(
        				yOnSurface, 
        				SNOW_BLOCK
        			)
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.SNOWY_SLOPES), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				steep, 
        				STONE
        			), 
        			snowUnderFloor, 
        			MaterialRules.condition(
        				yOnSurface, 
        				SNOW_BLOCK
        			)
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.JAGGED_PEAKS), 
        		STONE
        	),
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.GROVE), 
        		MaterialRules.sequence(
        			snowUnderFloor, 
        			DIRT
        		)
        	), 
        	stony, 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.WINDSWEPT_SAVANNA), 
        		MaterialRules.condition(
        			PresetSurfaceRuleData.surfaceNoiseAbove(1.75), 
        			STONE
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS), 
        		MaterialRules.sequence(
        			windswept,
        			DIRT
        			
//        			SurfaceRules.ifTrue(
//        				PresetSurfaceRuleData.surfaceNoiseAbove(2.0), 
//        				gravel
//        			), 
//        			SurfaceRules.ifTrue(
//        				PresetSurfaceRuleData.surfaceNoiseAbove(1.0), 
//        				STONE
//        			), 
//        			SurfaceRules.ifTrue(
//        				PresetSurfaceRuleData.surfaceNoiseAbove(-1.0), 
//        				DIRT
//        			), 
//        			gravel
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.MANGROVE_SWAMP), 
        		MUD
        	),
        	erodedDirt,
        	DIRT
        );
        MaterialRules.MaterialRule onFloor = MaterialRules.sequence(
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.FROZEN_PEAKS), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				steep, 
        				PACKED_ICE
        			), 
        			MaterialRules.condition(
        				MaterialRules.noiseThreshold(NoiseParametersKeys.PACKED_ICE, 0.0, 0.2), 
        				PACKED_ICE
        			), 
        			MaterialRules.condition(
        				MaterialRules.noiseThreshold(NoiseParametersKeys.ICE, 0.0, 0.025), 
        				ICE
        			), 
        			MaterialRules.condition(
        				yOnSurface, 
        				SNOW_BLOCK
        			)
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.SNOWY_SLOPES), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				steep, 
        				STONE
        			), 
        			snowOnFloor, 
        			MaterialRules.condition(
        				yOnSurface, 
        				SNOW_BLOCK
        			)
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.JAGGED_PEAKS), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				steep, 
        				DEEPSLATE
        			), 
        			MaterialRules.condition(
        				yOnSurface, 
        				SNOW_BLOCK
        			)
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.GROVE), 
        		MaterialRules.sequence(
        			snowOnFloor, 
        			MaterialRules.condition(
        				yOnSurface, 
        				SNOW_BLOCK
        			)
        		)
        	),
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.FOREST, BiomeKeys.DARK_FOREST),
        		makeForestRule(noise)
        	),
        	stony, 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.WINDSWEPT_SAVANNA), 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				PresetSurfaceRuleData.surfaceNoiseAbove(1.75), 
        				STONE
        			), 
        			MaterialRules.condition(
        				PresetSurfaceRuleData.surfaceNoiseAbove(-0.5), 
        				COARSE_DIRT
        			)
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS), 
        		MaterialRules.sequence(
            		windswept,
            		GRASS_BLOCK
//        			SurfaceRules.ifTrue(
//        				PresetSurfaceRuleData.surfaceNoiseAbove(2.0), 
//        				gravel
//        			), 
//        			SurfaceRules.ifTrue(
//        				PresetSurfaceRuleData.surfaceNoiseAbove(1.0),
//        				STONE
//        			),
//        			SurfaceRules.ifTrue(
//        				PresetSurfaceRuleData.surfaceNoiseAbove(-1.0),
//        				grass
//        			),
//        			gravel
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA),
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				PresetSurfaceRuleData.surfaceNoiseAbove(1.75), 
        				COARSE_DIRT
        			), 
        			MaterialRules.condition(
        				PresetSurfaceRuleData.surfaceNoiseAbove(-0.95), 
        				PODZOL
        			)
        		)
        	),
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.ICE_SPIKES), 
        		MaterialRules.condition(
        			yOnSurface,
        			SNOW_BLOCK
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.MANGROVE_SWAMP),
        		MUD
        	), 
        	MaterialRules.condition(
        		MaterialRules.biome(BiomeKeys.MUSHROOM_FIELDS),
        		MYCELIUM
        	), 
        	grass
        );
        MaterialRules.MaterialCondition surfaceNoise1 = MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, -0.909, -0.5454);
        MaterialRules.MaterialCondition surfaceNoise2 = MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, -0.1818, 0.1818);
        MaterialRules.MaterialCondition surfaceNoise3 = MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0.5454, 0.909);
        MaterialRules.MaterialRule surface = MaterialRules.sequence(
        	MaterialRules.condition(
        		y4BelowSurface, 
        		makeDesertRule(scaling, noise)
        	),
        	MaterialRules.condition(
        		MaterialRules.STONE_DEPTH_FLOOR, 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				MaterialRules.biome(BiomeKeys.WOODED_BADLANDS),
        				MaterialRules.condition(
	        				MaterialRules.not(erodedRock),
	        				MaterialRules.condition(
	        					below97, 
	        					MaterialRules.sequence(
	        						MaterialRules.condition(
	        							surfaceNoise1, 
	        							COARSE_DIRT
			        				), 
	        						MaterialRules.condition(
	        							surfaceNoise2, 
	        							COARSE_DIRT
			        				), 
	        						MaterialRules.condition(
	        							surfaceNoise3, 
	        							COARSE_DIRT
			        				), 
	        						grass
			        			)
	        				)
	        			)
        			),
        			MaterialRules.condition(
        				MaterialRules.biome(BiomeKeys.SWAMP), 
        				MaterialRules.condition(
        					below62, 
        					MaterialRules.condition(
        						MaterialRules.not(below63), 
        						MaterialRules.condition(
        							MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE_SWAMP, 0.0), 
        							WATER
        						)
        					)
        				)
        			), 
        			MaterialRules.condition(
        				MaterialRules.biome(BiomeKeys.MANGROVE_SWAMP), 
        				MaterialRules.condition(
        					below60, 
        					MaterialRules.condition(
        						MaterialRules.not(below63), 
        						MaterialRules.condition(
        							MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE_SWAMP, 0.0), 
        							WATER
        						)
        					)
        				)
        			)
        		)
        	), 
        	MaterialRules.condition(
        		badlands, 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				MaterialRules.STONE_DEPTH_FLOOR, 
        				MaterialRules.sequence(
        					MaterialRules.condition(
        						below256,
        						ORANGE_TERRACOTTA
        					), 
        					MaterialRules.condition(
        						above74, 
        						MaterialRules.sequence(
        							MaterialRules.condition(
        								surfaceNoise1, 
        								TERRACOTTA
        							), 
        							MaterialRules.condition(
        								surfaceNoise2, 
        								TERRACOTTA
        							), 
        							MaterialRules.condition(
        								surfaceNoise3, 
        								TERRACOTTA
        							), 
        							MaterialRules.terracottaBands()
        						)
        					), 
        					MaterialRules.condition(
        						y1BelowSurface,
        						MaterialRules.sequence(
        							MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, RED_SANDSTONE), 
        							RED_SAND
        						)
        					), 
        					MaterialRules.condition(
        						MaterialRules.not(hole), 
        						ORANGE_TERRACOTTA
        					), 
        					MaterialRules.condition(
        						y6BelowSurface, 
        						WHITE_TERRACOTTA
        					), 
        					gravel
        				)
        			), 
        			MaterialRules.condition(
        				above63, 
        				MaterialRules.sequence(
        					MaterialRules.condition(
        						below63, 
        						MaterialRules.condition(
        							MaterialRules.not(above74), 
        							ORANGE_TERRACOTTA
        						)
        					), 
        					MaterialRules.terracottaBands()
        				)
        			), 
        			MaterialRules.condition(
        				MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, 
        				MaterialRules.condition(
        					y6BelowSurface, 
        					WHITE_TERRACOTTA
        				)
        			)
        		)
        	), 
        	MaterialRules.condition(
        		MaterialRules.STONE_DEPTH_FLOOR, 
        		MaterialRules.condition(
        			y1BelowSurface, 
        			MaterialRules.sequence(
        				MaterialRules.condition(
        					frozenOcean, 
        					MaterialRules.condition(
        						hole, 
        						MaterialRules.sequence(
        							MaterialRules.condition(
        								yOnSurface, 
        								AIR
        							), 
        							MaterialRules.condition(
        								MaterialRules.temperature(), 
        								ICE
        							), 
        							WATER
        						)
        					)
        				),
        				MaterialRules.condition(
        					MaterialRules.not(erodedRock),
        					onFloor
        				)
        			)
        		)
        	), 
        	MaterialRules.condition(
        		y6BelowSurface, 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				MaterialRules.STONE_DEPTH_FLOOR, 
        				MaterialRules.condition(
        					frozenOcean, 
        					MaterialRules.condition(
        						hole, 
        						WATER
        					)
        				)
        			),
        			MaterialRules.condition(
        				MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
        				MaterialRules.condition(
        					MaterialRules.not(erodedRock),
        					underFloor
        				)
        			),
        			MaterialRules.condition(
        				sandyBeach,
        				MaterialRules.condition(
        					MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6, 
        					SANDSTONE
        				)
        			),
        			MaterialRules.condition(
        				desert, 
        				MaterialRules.condition(
        					MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_30, 
        					SANDSTONE
        				)
        			)
        		)
        	),
        	MaterialRules.condition(
        		MaterialRules.STONE_DEPTH_FLOOR, 
        		MaterialRules.sequence(
        			MaterialRules.condition(
        				MaterialRules.biome(BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS), 
        				STONE
        			), 
        			MaterialRules.condition(
        				MaterialRules.biome(BiomeKeys.WARM_OCEAN, BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN),
        				sand
        			),
        			MaterialRules.condition(
        				MaterialRules.not(erodedRock),
        				gravel
        			)
        		)
        	)//,
//        	SurfaceRules.ifTrue(
//            	erodedRock,
//            	SurfaceRules.ifTrue(
//            		SurfaceRules.stoneDepthCheck(strataBufferAmount, false, CaveSurface.FLOOR),
//            		makeStrataRule(strataBufferAmount, miscellaneousSettings, noise)
//            	)
//            )
        );
        List<MaterialRules.MaterialRule> list = Lists.newArrayList(
        	MaterialRules.condition(
        		MaterialRules.verticalGradient("bedrock_floor", YOffset.getBottom(), YOffset.aboveBottom(5)),
        		BEDROCK
        	),
        	MaterialRules.condition(
        		MaterialRules.surface(),
        		surface
        	),
//        	makeStrataRule(1, miscellaneousSettings, noise),
        	MaterialRules.condition(MaterialRules.verticalGradient("deepslate", YOffset.fixed(0), YOffset.fixed(8)), DEEPSLATE)
        );
        MaterialRules.MaterialRule rules = MaterialRules.sequence(list.toArray(MaterialRules.MaterialRule[]::new));
        return rules;
    }
    
    private static MaterialRules.MaterialRule makeDesertRule(Scaling scaling, RegistryEntryLookup<Noise> noise) {
    	RegistryEntry<Noise> variance = noise.getOrThrow(PresetSurfaceNoise.DESERT);
    	float min = scaling.ground(10);
    	float level = scaling.ground(40);
    	
    	MaterialRules.MaterialCondition aboveLevel = RTFSurfaceConditions.height(level, variance);
        MaterialRules.MaterialCondition desert = MaterialRules.biome(BiomeKeys.DESERT);
    	return MaterialRules.condition(
    		RTFSurfaceConditions.height(min),
    		MaterialRules.sequence(
    			MaterialRules.condition(
    				RTFSurfaceConditions.steepness(0.15F), 
			        MaterialRules.condition(
			        	desert, 
			        	MaterialRules.condition(
			        		aboveLevel, 
			        		MaterialRules.sequence(
								MaterialRules.condition(RTFSurfaceConditions.steepness(0.975F), TERRACOTTA),
								MaterialRules.condition(RTFSurfaceConditions.steepness(0.85F), BROWN_TERRACOTTA),
								MaterialRules.condition(RTFSurfaceConditions.steepness(0.75F), ORANGE_TERRACOTTA),
								MaterialRules.condition(RTFSurfaceConditions.steepness(0.65F), TERRACOTTA), 
								SMOOTH_SANDSTONE
							)
						)
    	            )
    			),
        		MaterialRules.condition(
        			RTFSurfaceConditions.steepness(0.3F), 
        			MaterialRules.condition(
        				desert, 
        				SMOOTH_SANDSTONE
        			)
            	)
    		)
    	);
    }
    
    private static MaterialRules.MaterialRule makeForestRule(RegistryEntryLookup<Noise> noise) {
    	return MaterialRules.condition(
    		MaterialRules.STONE_DEPTH_FLOOR, 
    		RTFSurfaceRules.noise(
    			noise.getOrThrow( PresetSurfaceNoise.FOREST),
    			List.of(
    				Pair.of(0.65F, PODZOL),
    				Pair.of(0.725F, DIRT)
    			)
    		)	
    	);
    }
    
	private static MaterialRules.MaterialRule makeStrataRule(int buffer, MiscellaneousSettings miscellaneousSettings, RegistryEntryLookup<Noise> noise) {
		List<StrataRule.Layer> layers = new ArrayList<>();
		
		RegistryEntry<Noise> depth = noise.getOrThrow(PresetStrataNoise.STRATA_DEPTH);
		layers.add(new StrataRule.Layer(RTFBlockTags.SOIL, depth, 3, 0, 1, 0.1F, 0.25F));
		layers.add(new StrataRule.Layer(RTFBlockTags.SEDIMENT, depth, 3, 0, 2, 0.05F, 0.15F));
		layers.add(new StrataRule.Layer(RTFBlockTags.CLAY, depth, 3, 0, 2, 0.05F, 0.1F));
		layers.add(new StrataRule.Layer(miscellaneousSettings.rockTag(), depth, 3, 10, 30, 0.1F, 1.5F));
		return new StrataRule(STRATA_CACHE_ID, buffer, 100, noise.getOrThrow(PresetSurfaceNoise.STRATA_REGION), layers);
	}

    private static MaterialRules.MaterialRule makeErodedDirtRule(RegistryEntryLookup<Noise> noise, SurfaceSettings.Erosion settings) {
    	return MaterialRules.condition(
    		RTFSurfaceConditions.steepness(settings.dirtSteepness, noise.getOrThrow(PresetSurfaceNoise.STEEPNESS_VARIANCE)),
    		MaterialRules.condition(
    			RTFSurfaceConditions.height(noise.getOrThrow(PresetSurfaceNoise.ERODED_DIRT), noise.getOrThrow(PresetSurfaceNoise.HEIGHT_VARIANCE)),
    			COARSE_DIRT
    		)
    	);
    }
	
    private static MaterialRules.MaterialCondition surfaceNoiseAbove(double target) {
        return MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, target / 8.25D, Double.MAX_VALUE);
    }
}