package org.haberno.terraloomed.data.preset;

import java.util.Map;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.data.preset.settings.MiscellaneousSettings;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.tags.RTFBiomeTags;
import raccoonman.reterraforged.world.worldgen.biome.modifier.BiomeModifier;
import raccoonman.reterraforged.world.worldgen.biome.modifier.BiomeModifiers;
import raccoonman.reterraforged.world.worldgen.biome.modifier.Filter;
import raccoonman.reterraforged.world.worldgen.biome.modifier.Order;

public class PresetBiomeModifierData {
	public static final RegistryKey<BiomeModifier> SWAMP_SURFACE = createKey("raw_generation/swamp_surface");
	public static final RegistryKey<BiomeModifier> ERODE_SNOW = createKey("modification/erode_snow");
	
	public static final RegistryKey<BiomeModifier> PLAINS_TREES = createKey("vegetation/trees/plains");
	public static final RegistryKey<BiomeModifier> FOREST_TREES = createKey("vegetation/trees/forest");
	public static final RegistryKey<BiomeModifier> FLOWER_FOREST_TREES = createKey("vegetation/trees/flower_forest");
	public static final RegistryKey<BiomeModifier> BIRCH_TREES = createKey("vegetation/trees/birch");
	public static final RegistryKey<BiomeModifier> DARK_FOREST_TREES = createKey("vegetation/trees/dark_forest");
	public static final RegistryKey<BiomeModifier> SAVANNA_TREES = createKey("vegetation/trees/savanna");
	public static final RegistryKey<BiomeModifier> SWAMP_TREES = createKey("vegetation/trees/swamp");
	public static final RegistryKey<BiomeModifier> MEADOW_TREES = createKey("vegetation/trees/meadow");
	public static final RegistryKey<BiomeModifier> FIR_TREES = createKey("vegetation/trees/fir");
	public static final RegistryKey<BiomeModifier> GROVE_TREES = createKey("vegetation/trees/grove");
	public static final RegistryKey<BiomeModifier> WINDSWEPT_HILLS_FIR_TREES = createKey("vegetation/trees/fir_windswept_hills");
	public static final RegistryKey<BiomeModifier> PINE_TREES = createKey("vegetation/trees/pine");
	public static final RegistryKey<BiomeModifier> SPRUCE_TREES = createKey("vegetation/trees/spruce");
	public static final RegistryKey<BiomeModifier> SPRUCE_TUNDRA_TREES = createKey("vegetation/trees/spruce_tundra");
	public static final RegistryKey<BiomeModifier> REDWOOD_TREES = createKey("vegetation/trees/redwood");
	public static final RegistryKey<BiomeModifier> JUNGLE_TREES = createKey("vegetation/trees/jungle");
	public static final RegistryKey<BiomeModifier> JUNGLE_EDGE_TREES = createKey("vegetation/trees/jungle_edge");
	public static final RegistryKey<BiomeModifier> BADLANDS_TREES = createKey("vegetation/trees/badlands");
	public static final RegistryKey<BiomeModifier> WOODED_BADLANDS_TREES = createKey("vegetation/trees/wooded_badlands");

	public static final RegistryKey<BiomeModifier> MARSH_BUSH = createKey("vegetation/bushes/marsh");
	public static final RegistryKey<BiomeModifier> PLAINS_BUSH = createKey("vegetation/bushes/plains");
	public static final RegistryKey<BiomeModifier> STEPPE_BUSH = createKey("vegetation/bushes/steppe");
	public static final RegistryKey<BiomeModifier> COLD_STEPPE_BUSH = createKey("vegetation/bushes/cold_steppe");
	public static final RegistryKey<BiomeModifier> TAIGA_SCRUB_BUSH = createKey("vegetation/bushes/taiga_scrub");
	
	public static final RegistryKey<BiomeModifier> FOREST_GRASS = createKey("vegetation/grass/forest");
	public static final RegistryKey<BiomeModifier> MEADOW_GRASS = createKey("vegetation/grass/meadow");
	public static final RegistryKey<BiomeModifier> FERN_GRASS = createKey("vegetation/grass/fern");
	public static final RegistryKey<BiomeModifier> BIRCH_GRASS = createKey("vegetation/grass/birch");
	
	public static void bootstrap(Preset preset, Registerable<BiomeModifier> ctx) {
		MiscellaneousSettings miscellaneous = preset.miscellaneous();
		RegistryEntryLookup<PlacedFeature> placedFeatures = ctx.lookup(RegistryKeys.PLACED_FEATURE);
		RegistryEntryLookup<Biome> biomes = ctx.lookup(RegistryKeys.BIOME);

		RegistryEntryList<Biome> hasSwampSurface = biomes.getOrThrow(RTFBiomeTags.HAS_SWAMP_SURFACE);
		
		ctx.register(SWAMP_SURFACE, prepend(GenerationStep.Feature.RAW_GENERATION, Filter.Behavior.WHITELIST, hasSwampSurface, placedFeatures.getOrThrow(PresetPlacedFeatures.SWAMP_SURFACE)));
		
		if(miscellaneous.naturalSnowDecorator || miscellaneous.smoothLayerDecorator) {
			ctx.register(ERODE_SNOW, append(GenerationStep.Feature.TOP_LAYER_MODIFICATION, placedFeatures.getOrThrow(PresetPlacedFeatures.ERODE_SNOW)));
		}
		
		if(miscellaneous.customBiomeFeatures) {
			RegistryEntryList<Biome> hasSwampTrees = biomes.getOrThrow(RTFBiomeTags.HAS_SWAMP_TREES);
			RegistryEntryList<Biome> hasPlainsTrees = biomes.getOrThrow(RTFBiomeTags.HAS_PLAINS_TREES);
			RegistryEntryList<Biome> hasForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_FOREST_TREES);
			RegistryEntryList<Biome> hasFlowerForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_FLOWER_FOREST_TREES);
			RegistryEntryList<Biome> hasBirchForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_BIRCH_FOREST_TREES);
			RegistryEntryList<Biome> hasDarkForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_DARK_FOREST_TREES);
			RegistryEntryList<Biome> hasSavannaTrees = biomes.getOrThrow(RTFBiomeTags.HAS_SAVANNA_TREES);
			RegistryEntryList<Biome> hasMeadowTrees = biomes.getOrThrow(RTFBiomeTags.HAS_MEADOW_TREES);
			RegistryEntryList<Biome> hasFirForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_FIR_FOREST_TREES);
			RegistryEntryList<Biome> hasGroveTrees = biomes.getOrThrow(RTFBiomeTags.HAS_GROVE_TREES);
			RegistryEntryList<Biome> hasWindsweptHillsTrees = biomes.getOrThrow(RTFBiomeTags.HAS_WINDSWEPT_HILLS_TREES);
			RegistryEntryList<Biome> hasPineForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_PINE_FOREST_TREES);
			RegistryEntryList<Biome> hasSpruceForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_SPRUCE_FOREST_TREES);
			RegistryEntryList<Biome> hasSpruceTundraTrees = biomes.getOrThrow(RTFBiomeTags.HAS_SPRUCE_TUNDRA_TREES);
			RegistryEntryList<Biome> hasRedwoodForestTrees = biomes.getOrThrow(RTFBiomeTags.HAS_REDWOOD_FOREST_TREES);
			RegistryEntryList<Biome> hasJungleTrees = biomes.getOrThrow(RTFBiomeTags.HAS_JUNGLE_TREES);
			RegistryEntryList<Biome> hasJungleEdgeTrees = biomes.getOrThrow(RTFBiomeTags.HAS_JUNGLE_EDGE_TREES);
			RegistryEntryList<Biome> hasBadlandsTrees = biomes.getOrThrow(RTFBiomeTags.HAS_BADLANDS_TREES);
			RegistryEntryList<Biome> hasWoodedBadlandsTrees = biomes.getOrThrow(RTFBiomeTags.HAS_WOODED_BADLANDS_TREES);
			
			RegistryEntry<PlacedFeature> plainsTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.PLAINS_TREES);
			RegistryEntry<PlacedFeature> forestTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.FOREST_TREES);
			RegistryEntry<PlacedFeature> flowerForestTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.FLOWER_FOREST_TREES);
			RegistryEntry<PlacedFeature> birchTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.BIRCH_TREES);
			RegistryEntry<PlacedFeature> darkForestTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.DARK_FOREST_TREES);
			RegistryEntry<PlacedFeature> savannaTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.SAVANNA_TREES);
			RegistryEntry<PlacedFeature> swampTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.SWAMP_TREES);
			RegistryEntry<PlacedFeature> meadowTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.MEADOW_TREES);
			RegistryEntry<PlacedFeature> firTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.FIR_TREES);
			RegistryEntry<PlacedFeature> groveTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.GROVE_TREES);
			RegistryEntry<PlacedFeature> windsweptHillsFirTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.WINDSWEPT_HILLS_FIR_TREES);
			RegistryEntry<PlacedFeature> pineTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.PINE_TREES);
			RegistryEntry<PlacedFeature> spruceTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.SPRUCE_TREES);
			RegistryEntry<PlacedFeature> spruceTundraTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.SPRUCE_TUNDRA_TREES);
			RegistryEntry<PlacedFeature> redwoodTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.REDWOOD_TREES);
			RegistryEntry<PlacedFeature> jungleTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.JUNGLE_TREES);
			RegistryEntry<PlacedFeature> jungleEdgeTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.JUNGLE_EDGE_TREES);
			RegistryEntry<PlacedFeature> badlandsTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.BADLANDS_TREES);
			RegistryEntry<PlacedFeature> woodedBadlandsTrees = placedFeatures.getOrThrow(PresetPlacedFeatures.WOODED_BADLANDS_TREES);
			
			ctx.register(PLAINS_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasPlainsTrees, Map.of(
				VegetationPlacedFeatures.TREES_PLAINS, plainsTrees,
				VegetationPlacedFeatures.TREES_WATER, plainsTrees
			)));
			ctx.register(FOREST_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasForestTrees, Map.of(
				VegetationPlacedFeatures.TREES_BIRCH_AND_OAK, forestTrees
			)));
			ctx.register(FLOWER_FOREST_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasFlowerForestTrees, Map.of(
				VegetationPlacedFeatures.TREES_FLOWER_FOREST, flowerForestTrees
			)));
			ctx.register(BIRCH_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasBirchForestTrees, Map.of(
				VegetationPlacedFeatures.TREES_BIRCH, birchTrees,
				VegetationPlacedFeatures.BIRCH_TALL, birchTrees
			)));
			ctx.register(DARK_FOREST_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasDarkForestTrees, Map.of(
				VegetationPlacedFeatures.DARK_FOREST_VEGETATION, darkForestTrees
			)));
			ctx.register(SAVANNA_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasSavannaTrees, Map.of(
				VegetationPlacedFeatures.TREES_SAVANNA, savannaTrees
			)));
			ctx.register(SWAMP_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasSwampTrees, Map.of(
				VegetationPlacedFeatures.TREES_SWAMP, swampTrees
			)));
			ctx.register(MEADOW_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasMeadowTrees, Map.of(
				VegetationPlacedFeatures.TREES_MEADOW, meadowTrees
			)));
			ctx.register(FIR_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasFirForestTrees, Map.of(
				VegetationPlacedFeatures.TREES_WINDSWEPT_FOREST, firTrees
			)));
			ctx.register(GROVE_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasGroveTrees, Map.of(
				VegetationPlacedFeatures.TREES_GROVE, groveTrees
			)));
			ctx.register(WINDSWEPT_HILLS_FIR_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasWindsweptHillsTrees, Map.of(
				VegetationPlacedFeatures.TREES_WINDSWEPT_HILLS, windsweptHillsFirTrees
			)));
			ctx.register(PINE_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasPineForestTrees, Map.of(
				VegetationPlacedFeatures.TREES_TAIGA, pineTrees,
				VegetationPlacedFeatures.TREES_OLD_GROWTH_SPRUCE_TAIGA, pineTrees
			)));
			ctx.register(SPRUCE_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasSpruceForestTrees, Map.of(
				VegetationPlacedFeatures.TREES_TAIGA, spruceTrees
			)));
			ctx.register(SPRUCE_TUNDRA_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasSpruceTundraTrees, Map.of(
				VegetationPlacedFeatures.TREES_SNOWY, spruceTundraTrees,
				VegetationPlacedFeatures.TREES_WATER, spruceTundraTrees
			)));
			ctx.register(REDWOOD_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasRedwoodForestTrees, Map.of(
				VegetationPlacedFeatures.TREES_OLD_GROWTH_PINE_TAIGA, redwoodTrees
			)));
			ctx.register(JUNGLE_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasJungleTrees, Map.of(
				VegetationPlacedFeatures.TREES_JUNGLE, jungleTrees,
				VegetationPlacedFeatures.BAMBOO_VEGETATION, jungleTrees
			)));
			ctx.register(JUNGLE_EDGE_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasJungleEdgeTrees, Map.of(
				VegetationPlacedFeatures.TREES_SPARSE_JUNGLE, jungleEdgeTrees
			)));
			ctx.register(BADLANDS_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasBadlandsTrees, Map.of(
				VegetationPlacedFeatures.TREES_BADLANDS, badlandsTrees,
				VegetationPlacedFeatures.TREES_WINDSWEPT_SAVANNA, badlandsTrees
			)));
			ctx.register(WOODED_BADLANDS_TREES, BiomeModifiers.replace(GenerationStep.Feature.VEGETAL_DECORATION, hasWoodedBadlandsTrees, Map.of(
				VegetationPlacedFeatures.TREES_BADLANDS, woodedBadlandsTrees
			)));

			RegistryEntryList<Biome> hasMarshBushes = biomes.getOrThrow(RTFBiomeTags.HAS_MARSH_BUSHES);
			RegistryEntryList<Biome> hasPlainsBushes = biomes.getOrThrow(RTFBiomeTags.HAS_PLAINS_BUSHES);
			RegistryEntryList<Biome> hasSteppeBushes = biomes.getOrThrow(RTFBiomeTags.HAS_STEPPE_BUSHES);
			RegistryEntryList<Biome> hasColdSteppeBushes = biomes.getOrThrow(RTFBiomeTags.HAS_COLD_STEPPE_BUSHES);
			RegistryEntryList<Biome> hasColdTaigaScrubBushes = biomes.getOrThrow(RTFBiomeTags.HAS_COLD_TAIGA_SCRUB_BUSHES);
			
			ctx.register(MARSH_BUSH, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasMarshBushes, placedFeatures.getOrThrow(PresetPlacedFeatures.MARSH_BUSH)));
			ctx.register(PLAINS_BUSH, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasPlainsBushes, placedFeatures.getOrThrow(PresetPlacedFeatures.PLAINS_BUSH)));
			ctx.register(STEPPE_BUSH, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasSteppeBushes, placedFeatures.getOrThrow(PresetPlacedFeatures.STEPPE_BUSH)));
			ctx.register(COLD_STEPPE_BUSH, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasColdSteppeBushes, placedFeatures.getOrThrow(PresetPlacedFeatures.COLD_STEPPE_BUSH)));
			ctx.register(TAIGA_SCRUB_BUSH, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasColdTaigaScrubBushes, placedFeatures.getOrThrow(PresetPlacedFeatures.TAIGA_SCRUB_BUSH)));
			
			RegistryEntryList<Biome> hasForestGrass = biomes.getOrThrow(RTFBiomeTags.HAS_FOREST_GRASS);
			RegistryEntryList<Biome> hasMeadowGrass = biomes.getOrThrow(RTFBiomeTags.HAS_MEADOW_GRASS);
			RegistryEntryList<Biome> hasColdGrass = biomes.getOrThrow(RTFBiomeTags.HAS_FERN_GRASS);
			RegistryEntryList<Biome> hasBirchGrass = biomes.getOrThrow(RTFBiomeTags.HAS_BIRCH_GRASS);
			
			ctx.register(FOREST_GRASS, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasForestGrass, placedFeatures.getOrThrow(PresetPlacedFeatures.FOREST_GRASS)));
			ctx.register(MEADOW_GRASS, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasMeadowGrass, placedFeatures.getOrThrow(PresetPlacedFeatures.MEADOW_GRASS)));
			ctx.register(FERN_GRASS, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasColdGrass, placedFeatures.getOrThrow(PresetPlacedFeatures.FERN_GRASS)));
			ctx.register(BIRCH_GRASS, prepend(GenerationStep.Feature.VEGETAL_DECORATION, Filter.Behavior.WHITELIST, hasBirchGrass, placedFeatures.getOrThrow(PresetPlacedFeatures.BIRCH_GRASS)));
		}
	}
	
	@SafeVarargs
	private static BiomeModifier prepend(GenerationStep.Feature step, RegistryEntry<PlacedFeature>... features) {
		return BiomeModifiers.add(Order.PREPEND, step, RegistryEntryList.of(features));
	}

	@SafeVarargs
	private static BiomeModifier prepend(GenerationStep.Feature step, Filter.Behavior filterBehavior, RegistryEntryList<Biome> biomes, RegistryEntry<PlacedFeature>... features) {
		return BiomeModifiers.add(Order.PREPEND, step, filterBehavior, biomes, RegistryEntryList.of(features));
	}

	@SafeVarargs
	private static BiomeModifier append(GenerationStep.Feature step, RegistryEntry<PlacedFeature>... features) {
		return BiomeModifiers.add(Order.APPEND, step, RegistryEntryList.of(features));
	}
	
	@SafeVarargs
	private static BiomeModifier append(GenerationStep.Feature step, Filter.Behavior filterBehavior, RegistryEntryList<Biome> biomes, RegistryEntry<PlacedFeature>... features) {
		return BiomeModifiers.add(Order.APPEND, step, filterBehavior, biomes, RegistryEntryList.of(features));
	}
	
	private static RegistryKey<BiomeModifier> createKey(String name) {
        return RegistryKey.of(RTFRegistries.BIOME_MODIFIER, RTFCommon.location(name));
	}
}
