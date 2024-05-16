package org.haberno.terraloomed.data.preset;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.haberno.terraloomed.RTFCommon;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.data.preset.settings.SurfaceSettings;
import org.haberno.terraloomed.data.preset.settings.WorldSettings;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.tags.RTFBiomeTags;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.surface.condition.RTFSurfaceConditions;
import org.haberno.terraloomed.worldgen.surface.rule.LayeredSurfaceRule;
import org.haberno.terraloomed.worldgen.util.Scaling;

public class PresetSurfaceLayerData {
	private static final MaterialRules.MaterialRule ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
	private static final MaterialRules.MaterialRule BROWN_TERRACOTTA = makeStateRule(Blocks.BROWN_TERRACOTTA);
	private static final MaterialRules.MaterialRule TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
    private static final MaterialRules.MaterialRule SMOOTH_SANDSTONE = makeStateRule(Blocks.SMOOTH_SANDSTONE);

	private static final MaterialRules.MaterialRule GRASS = makeStateRule(Blocks.GRASS_BLOCK);
	private static final MaterialRules.MaterialRule DIRT = makeStateRule(Blocks.DIRT);
	private static final MaterialRules.MaterialRule PODZOL = makeStateRule(Blocks.PODZOL);
	private static final MaterialRules.MaterialRule STONE = makeStateRule(Blocks.STONE);
    private static final MaterialRules.MaterialRule COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final MaterialRules.MaterialRule GRAVEL = makeStateRule(Blocks.GRAVEL);
    private static final MaterialRules.MaterialRule SAND = makeStateRule(Blocks.SAND);
	
	public static void bootstrap(Preset preset, Registerable<LayeredSurfaceRule.Layer> ctx) {
		WorldSettings worldSettings = preset.world();
		WorldSettings.Properties properties = worldSettings.properties;
		
		SurfaceSettings surfaceSettings = preset.surface();
		SurfaceSettings.Erosion erosion = surfaceSettings.erosion();
		
		Scaling scaling = Scaling.make(properties.terrainScaler(), properties.seaLevel);
		
	}
	
	private static LayeredSurfaceRule.Layer makeRockErosion() {
		return LayeredSurfaceRule.layer(STONE);
	}

    private static LayeredSurfaceRule.Layer makeDirtErosion(RegistryEntryLookup<Noise> noise, SurfaceSettings.Erosion settings) {
		return LayeredSurfaceRule.layer(
	    	MaterialRules.condition(
	    		erosionBiomeCheck(),
	    		MaterialRules.condition(
		    		RTFSurfaceConditions.steepness(settings.dirtSteepness, noise.getOrThrow(PresetSurfaceNoise.STEEPNESS_VARIANCE)),
		    		MaterialRules.condition(
		    			RTFSurfaceConditions.height(noise.getOrThrow(PresetSurfaceNoise.ERODED_DIRT), noise.getOrThrow(PresetSurfaceNoise.HEIGHT_VARIANCE)),
		    			COARSE_DIRT
		    		)
		    	)
        	)
    	);
    }

	private static LayeredSurfaceRule.Layer makeBadlandsErosion() {
		return LayeredSurfaceRule.layer(
			MaterialRules.condition(
				MaterialRules.biome(BiomeKeys.WOODED_BADLANDS),
				MaterialRules.terracottaBands()
			)
		);
	}
	
//	private static LayeredSurfaceRule.Layer makeErosion(SurfaceSettings.Erosion erosion, HolderGetter<Noise> noise) {
//		SurfaceRules.ConditionSource erodedRock = RTFSurfaceConditions.steepness(erosion.rockSteepness, noise.getOrThrow(PresetSurfaceNoise.STEEPNESS_VARIANCE));
//		SurfaceRules.ConditionSource erodedRockVariance = RTFSurfaceConditions.height(noise.getOrThrow(PresetSurfaceNoise.ERODED_ROCK), noise.getOrThrow(PresetSurfaceNoise.HEIGHT_VARIANCE));
//		SurfaceRules.RuleSource erodedMaterial = RTFSurfaceRules.layered(RTFSurfaceLayerTags.EROSION_MATERIAL);
//		SurfaceRules.RuleSource erode = SurfaceRules.sequence(
//			SurfaceRules.ifTrue(
//				erodedRock, 
//				erodedMaterial
//			),	
//			SurfaceRules.ifTrue(
//				erodedRockVariance,
//				erodedMaterial
//			)
//		);
//		return LayeredSurfaceRule.layer(
//			SurfaceRuleData.overworld(),
//			SurfaceRules.ifTrue(
//				SurfaceRules.abovePreliminarySurface(),
//				SurfaceRules.sequence(
//					SurfaceRules.ifTrue(
//						SurfaceRules.ON_FLOOR, 
//						erode
//					),
//					SurfaceRules.ifTrue(
//						SurfaceRules.UNDER_FLOOR,
//						erode
//					)
//				)
//			)
//		);
//	}
	
	private static LayeredSurfaceRule.Layer makeDesert(Scaling scaling, RegistryEntryLookup<Noise> noise) {
    	RegistryEntry<Noise> variance = noise.getOrThrow(PresetSurfaceNoise.DESERT);
    	float min = scaling.ground(10);
    	float level = scaling.ground(40);
    	
    	MaterialRules.MaterialCondition aboveLevel = RTFSurfaceConditions.height(level, variance);
		return LayeredSurfaceRule.layer(
	    	MaterialRules.condition(
	    		MaterialRules.biome(BiomeKeys.DESERT),
	    		MaterialRules.condition(
		    		RTFSurfaceConditions.height(min), 
		    		MaterialRules.sequence(
		    			MaterialRules.condition(
		    				RTFSurfaceConditions.steepness(0.15F), 
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
		    			),
		        		MaterialRules.condition(
		        			RTFSurfaceConditions.steepness(0.3F), 
		        			SMOOTH_SANDSTONE
		            	)
		    		)
		    	)
	    	)
    	);
    }


	
    private static MaterialRules.MaterialCondition erosionBiomeCheck() {
    	return MaterialRules.not(RTFSurfaceConditions.biomeTag(RTFBiomeTags.EROSION_BLACKLIST));
    }
    
    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }

    public static RegistryKey<LayeredSurfaceRule.Layer> createKey(String name) {
        return RegistryKey.of(RTFRegistries.SURFACE_LAYERS, RTFCommon.location(name));
	}
}
