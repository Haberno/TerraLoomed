package org.haberno.terraloomed.data.preset;

import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.MiscConfiguredFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureEntry;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import net.minecraft.world.gen.feature.TreePlacedFeatures;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.PredicatedStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.data.preset.settings.MiscellaneousSettings;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.data.preset.settings.SurfaceSettings;
import raccoonman.reterraforged.data.preset.settings.TerrainSettings;
import raccoonman.reterraforged.world.worldgen.feature.BushFeature;
import raccoonman.reterraforged.world.worldgen.feature.ErodeSnowFeature;
import raccoonman.reterraforged.world.worldgen.feature.RTFFeatures;
import raccoonman.reterraforged.world.worldgen.feature.SwampSurfaceFeature;
import raccoonman.reterraforged.world.worldgen.feature.chance.ChanceFeature;
import raccoonman.reterraforged.world.worldgen.feature.chance.ChanceModifier;
import raccoonman.reterraforged.world.worldgen.feature.chance.RTFChanceModifiers;
import raccoonman.reterraforged.world.worldgen.feature.template.TemplateFeature;
import raccoonman.reterraforged.world.worldgen.feature.template.decorator.DecoratorConfig;
import raccoonman.reterraforged.world.worldgen.feature.template.decorator.TemplateDecorator;
import raccoonman.reterraforged.world.worldgen.feature.template.decorator.TemplateDecorators;
import raccoonman.reterraforged.world.worldgen.feature.template.decorator.TreeContext;
import raccoonman.reterraforged.world.worldgen.feature.template.paste.PasteConfig;
import raccoonman.reterraforged.world.worldgen.feature.template.placement.TemplatePlacement;
import raccoonman.reterraforged.world.worldgen.feature.template.placement.TemplatePlacements;
import raccoonman.reterraforged.world.worldgen.feature.template.template.TemplateContext;

public class PresetConfiguredFeatures {
	public static final RegistryKey<ConfiguredFeature<?, ?>> ERODE_SNOW = createKey("erode_snow");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SWAMP_SURFACE = createKey("swamp_surface");

	public static final RegistryKey<ConfiguredFeature<?, ?>> OAK_SMALL = createKey("oak/small");
	public static final RegistryKey<ConfiguredFeature<?, ?>> OAK_FOREST = createKey("oak/forest");
	public static final RegistryKey<ConfiguredFeature<?, ?>> OAK_LARGE = createKey("oak/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> BIRCH_SMALL = createKey("birch/small");
	public static final RegistryKey<ConfiguredFeature<?, ?>> BIRCH_FOREST = createKey("birch/forest");
	public static final RegistryKey<ConfiguredFeature<?, ?>> BIRCH_LARGE = createKey("birch/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ACACIA_SMALL = createKey("acacia/small");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ACACIA_LARGE = createKey("acacia/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_OAK_SMALL = createKey("dark_oak/small");
	public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_OAK_LARGE = createKey("dark_oak/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> HUGE_BROWN_MUSHROOM = createKey("mushrooms/huge_brown_mushroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> HUGE_RED_MUSHROOM = createKey("mushrooms/huge_red_mushroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> WILLOW_SMALL = createKey("willow/small");
	public static final RegistryKey<ConfiguredFeature<?, ?>> WILLOW_LARGE = createKey("willow/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MEADOW_NORMAL = createKey("meadow/normal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MEADOW_VARIANT = createKey("meadow/variant");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PINE = createKey("pine/pine");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SPRUCE_SMALL = createKey("spruce/small");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SPRUCE_LARGE = createKey("spruce/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SPRUCE_SMALL_ON_SNOW = createKey("spruce/small_on_snow");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SPRUCE_LARGE_ON_SNOW = createKey("spruce/large_on_snow");
	public static final RegistryKey<ConfiguredFeature<?, ?>> REDWOOD_LARGE = createKey("redwood/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> REDWOOD_HUGE = createKey("redwood/huge");
	public static final RegistryKey<ConfiguredFeature<?, ?>> JUNGLE_SMALL = createKey("jungle/small");
	public static final RegistryKey<ConfiguredFeature<?, ?>> JUNGLE_LARGE = createKey("jungle/large");
	public static final RegistryKey<ConfiguredFeature<?, ?>> JUNGLE_HUGE = createKey("jungle/huge");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ACACIA_BUSH = createKey("acacia/bush");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MARSH_BUSH = createKey("shrubs/marsh_bush");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PLAINS_BUSH = createKey("shrubs/plains_bush");
	public static final RegistryKey<ConfiguredFeature<?, ?>> STEPPE_BUSH = createKey("shrubs/steppe_bush");
	public static final RegistryKey<ConfiguredFeature<?, ?>> COLD_STEPPE_BUSH = createKey("shrubs/cold_steppe_bush");
	public static final RegistryKey<ConfiguredFeature<?, ?>> TAIGA_SCRUB_BUSH = createKey("shrubs/taiga_scrub_bush");

	public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_GRASS = createKey("forest_grass");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MEADOW_GRASS = createKey("meadow_grass");
	public static final RegistryKey<ConfiguredFeature<?, ?>> FERN_GRASS = createKey("fern_grass");
	public static final RegistryKey<ConfiguredFeature<?, ?>> BIRCH_GRASS = createKey("birch_grass");
	
	public static final RegistryKey<ConfiguredFeature<?, ?>> PLAINS_TREES = createKey("plains_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_TREES = createKey("forest_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_FOREST_TREES = createKey("flower_forest_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> BIRCH_TREES = createKey("birch_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_FOREST_TREES = createKey("dark_forest_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SAVANNA_TREES = createKey("savanna_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> BADLANDS_TREES = createKey("badlands_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> WOODED_BADLANDS_TREES = createKey("wooded_badlands_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SWAMP_TREES = createKey("swamp_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MEADOW_TREES = createKey("meadow_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> FIR_TREES = createKey("fir_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> GROVE_TREES = createKey("grove_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SPRUCE_TREES = createKey("spruce_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SPRUCE_TUNDRA_TREES = createKey("spruce_tundra_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> REDWOOD_TREES = createKey("redwood_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> JUNGLE_TREES = createKey("jungle_trees");
	public static final RegistryKey<ConfiguredFeature<?, ?>> JUNGLE_EDGE_TREES = createKey("jungle_edge_trees");
	
	public static void bootstrap(Preset preset, Registerable<ConfiguredFeature<?, ?>> ctx) {
		MiscellaneousSettings miscellaneous = preset.miscellaneous();
		SurfaceSettings surface = preset.surface();
		SurfaceSettings.Erosion erosion = surface.erosion();

		TerrainSettings terrain = preset.terrain();
		TerrainSettings.General general = terrain.general;
		
		if(miscellaneous.naturalSnowDecorator || miscellaneous.smoothLayerDecorator) {
//			ErodeFeature.Config erodeConfig = new ErodeFeature.Config(miscellaneous.rockTag(), erosion.rockVariance, erosion.rockMin, erosion.dirtVariance, erosion.dirtMin, erosion.rockSteepness, erosion.dirtSteepness, erosion.screeSteepness, erosion.heightModifier / 255F, erosion.slopeModifier / 255F, 256, 3F / 255F, 0.55F);
			ConfiguredFeatures.register(ctx, ERODE_SNOW, RTFFeatures.ERODE_SNOW, new ErodeSnowFeature.Config(erosion.snowSteepness, (float) erosion.snowHeight / 255.0F, miscellaneous.naturalSnowDecorator, miscellaneous.smoothLayerDecorator, erosion.heightModifier / 255F, erosion.slopeModifier / 255F));
		}
		
		ConfiguredFeatures.register(ctx, SWAMP_SURFACE, RTFFeatures.SWAMP_SURFACE, new SwampSurfaceFeature.Config(Blocks.CLAY.getDefaultState(), Blocks.MUD.getDefaultState(), Blocks.MUD.getDefaultState()));
		
		if(miscellaneous.customBiomeFeatures) {
			RegistryEntryLookup<PlacedFeature> placedFeatures = ctx.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
			RegistryEntry<PlacedFeature> oakSmall = placedFeatures.getOrThrow(PresetPlacedFeatures.OAK_SMALL);
			RegistryEntry<PlacedFeature> oakForest = placedFeatures.getOrThrow(PresetPlacedFeatures.OAK_FOREST);
			RegistryEntry<PlacedFeature> oakLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.OAK_LARGE);
			RegistryEntry<PlacedFeature> birchSmall = placedFeatures.getOrThrow(PresetPlacedFeatures.BIRCH_SMALL);
			RegistryEntry<PlacedFeature> birchForest = placedFeatures.getOrThrow(PresetPlacedFeatures.BIRCH_FOREST);
			RegistryEntry<PlacedFeature> birchLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.BIRCH_LARGE);
			RegistryEntry<PlacedFeature> acaciaBush = placedFeatures.getOrThrow(PresetPlacedFeatures.ACACIA_BUSH);
			RegistryEntry<PlacedFeature> acaciaSmall = placedFeatures.getOrThrow(PresetPlacedFeatures.ACACIA_SMALL);
			RegistryEntry<PlacedFeature> acaciaLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.ACACIA_LARGE);
			RegistryEntry<PlacedFeature> darkOakSmall = placedFeatures.getOrThrow(PresetPlacedFeatures.DARK_OAK_SMALL);
			RegistryEntry<PlacedFeature> darkOakLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.DARK_OAK_LARGE);
			RegistryEntry<PlacedFeature> hugeBrownMushroom = placedFeatures.getOrThrow(PresetPlacedFeatures.HUGE_BROWN_MUSHROOM);
			RegistryEntry<PlacedFeature> hugeRedMushroom = placedFeatures.getOrThrow(PresetPlacedFeatures.HUGE_RED_MUSHROOM);
			RegistryEntry<PlacedFeature> willowSmall = placedFeatures.getOrThrow(PresetPlacedFeatures.WILLOW_SMALL);
			RegistryEntry<PlacedFeature> willowLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.WILLOW_LARGE);
			RegistryEntry<PlacedFeature> meadowNormal = placedFeatures.getOrThrow(PresetPlacedFeatures.MEADOW_NORMAL);
			RegistryEntry<PlacedFeature> meadowVariant = placedFeatures.getOrThrow(PresetPlacedFeatures.MEADOW_VARIANT);
			RegistryEntry<PlacedFeature> spruceSmall = placedFeatures.getOrThrow(PresetPlacedFeatures.SPRUCE_SMALL);
			RegistryEntry<PlacedFeature> spruceLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.SPRUCE_LARGE);
			RegistryEntry<PlacedFeature> spruceSmallOnSnow = placedFeatures.getOrThrow(PresetPlacedFeatures.SPRUCE_SMALL_ON_SNOW);
			RegistryEntry<PlacedFeature> spruceLargeOnSnow = placedFeatures.getOrThrow(PresetPlacedFeatures.SPRUCE_LARGE_ON_SNOW);
			RegistryEntry<PlacedFeature> redwoodLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.REDWOOD_LARGE);
			RegistryEntry<PlacedFeature> redwoodHuge = placedFeatures.getOrThrow(PresetPlacedFeatures.REDWOOD_HUGE);
			RegistryEntry<PlacedFeature> jungleSmall = placedFeatures.getOrThrow(PresetPlacedFeatures.JUNGLE_SMALL);
			RegistryEntry<PlacedFeature> jungleLarge = placedFeatures.getOrThrow(PresetPlacedFeatures.JUNGLE_LARGE);
			RegistryEntry<PlacedFeature> jungleHuge = placedFeatures.getOrThrow(PresetPlacedFeatures.JUNGLE_HUGE);
			RegistryEntry<PlacedFeature> jungleBush = placedFeatures.getOrThrow(TreePlacedFeatures.JUNGLE_BUSH);

			ConfiguredFeatures.register(ctx, OAK_SMALL, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.OAK_SMALL));
			ConfiguredFeatures.register(ctx, OAK_FOREST, RTFFeatures.TEMPLATE, makeTreeWithBehives(PresetTemplatePaths.OAK_FOREST, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005, ImmutableMap.of(
				BiomeKeys.PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.SUNFLOWER_PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.FLOWER_FOREST, PresetTemplateDecoratorLists.BEEHIVE_RARITY_002_AND_005
			)));
			ConfiguredFeatures.register(ctx, OAK_LARGE, RTFFeatures.TEMPLATE, makeTreeWithBehives(PresetTemplatePaths.OAK_LARGE, PresetTemplateDecoratorLists.BEEHIVE_RARITY_0002_AND_005, ImmutableMap.of(
				BiomeKeys.PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.SUNFLOWER_PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.FLOWER_FOREST, PresetTemplateDecoratorLists.BEEHIVE_RARITY_002_AND_005
			)));
			ConfiguredFeatures.register(ctx, BIRCH_SMALL, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.BIRCH_SMALL));
			ConfiguredFeatures.register(ctx, BIRCH_FOREST, RTFFeatures.TEMPLATE, makeTreeWithBehives(PresetTemplatePaths.BIRCH_FOREST, PresetTemplateDecoratorLists.BEEHIVE_RARITY_0002_AND_005, ImmutableMap.of(
				BiomeKeys.PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.SUNFLOWER_PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.FLOWER_FOREST, PresetTemplateDecoratorLists.BEEHIVE_RARITY_002_AND_005
			)));
			ConfiguredFeatures.register(ctx, BIRCH_LARGE, RTFFeatures.TEMPLATE, makeTreeWithBehives(PresetTemplatePaths.BIRCH_LARGE, PresetTemplateDecoratorLists.BEEHIVE_RARITY_0002_AND_005, ImmutableMap.of(
				BiomeKeys.PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.SUNFLOWER_PLAINS, PresetTemplateDecoratorLists.BEEHIVE_RARITY_005,
				BiomeKeys.FLOWER_FOREST, PresetTemplateDecoratorLists.BEEHIVE_RARITY_002_AND_005
			)));
			ConfiguredFeatures.register(ctx, ACACIA_SMALL, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.ACACIA_SMALL));
			ConfiguredFeatures.register(ctx, ACACIA_LARGE, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.ACACIA_LARGE));
			ConfiguredFeatures.register(ctx, DARK_OAK_SMALL, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.DARK_OAK_SMALL));
			ConfiguredFeatures.register(ctx, DARK_OAK_LARGE, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.DARK_OAK_LARGE));
			ConfiguredFeatures.register(ctx, HUGE_BROWN_MUSHROOM, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.BROWN_MUSHROOM, 0));
			ConfiguredFeatures.register(ctx, HUGE_RED_MUSHROOM, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.RED_MUSHROOM));
			ConfiguredFeatures.register(ctx, WILLOW_SMALL, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.WILLOW_SMALL));
			ConfiguredFeatures.register(ctx, WILLOW_LARGE, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.WILLOW_LARGE));
			ConfiguredFeatures.register(ctx, MEADOW_NORMAL, RTFFeatures.TEMPLATE, makeTreeWithBehives(PresetTemplatePaths.MEADOW_NORMAL, PresetTemplateDecoratorLists.BEEHIVE_RARITY_0075));
			ConfiguredFeatures.register(ctx, MEADOW_VARIANT, RTFFeatures.TEMPLATE, makeTreeWithBehives(PresetTemplatePaths.MEADOW_VARIANT, PresetTemplateDecoratorLists.BEEHIVE_RARITY_0075));
			TemplateFeature.Config<?> pineConfig = makeTree(PresetTemplatePaths.PINE);
			ConfiguredFeatures.register(ctx, PINE, RTFFeatures.TEMPLATE, pineConfig);
			ConfiguredFeatures.register(ctx, SPRUCE_SMALL, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.SPRUCE_SMALL));
			ConfiguredFeatures.register(ctx, SPRUCE_LARGE, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.SPRUCE_LARGE));
			ConfiguredFeatures.register(ctx, SPRUCE_SMALL_ON_SNOW, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.SPRUCE_SMALL, TemplatePlacements.any(), 3));
			ConfiguredFeatures.register(ctx, SPRUCE_LARGE_ON_SNOW, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.SPRUCE_LARGE, TemplatePlacements.any(), 3));
			ConfiguredFeatures.register(ctx, REDWOOD_LARGE, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.REDWOOD_LARGE));
			TemplateFeature.Config<?> redwoodHugeConfig = makeTree(PresetTemplatePaths.REDWOOD_HUGE, ImmutableList.of(TemplateDecorators.tree(new AlterGroundTreeDecorator(SimpleBlockStateProvider.of(Blocks.PODZOL)))), TemplatePlacements.tree(), 3);
			ConfiguredFeatures.register(ctx, REDWOOD_HUGE, RTFFeatures.TEMPLATE, redwoodHugeConfig);
			TemplateFeature.Config<?> jungleSmallConfig = makeTree(PresetTemplatePaths.JUNGLE_SMALL);
			ConfiguredFeatures.register(ctx, JUNGLE_SMALL, RTFFeatures.TEMPLATE, jungleSmallConfig);
			ConfiguredFeatures.register(ctx, JUNGLE_LARGE, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.JUNGLE_LARGE));
			ConfiguredFeatures.register(ctx, JUNGLE_HUGE, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.JUNGLE_HUGE));
			
			ConfiguredFeatures.register(ctx, ACACIA_BUSH, RTFFeatures.TEMPLATE, makeTree(PresetTemplatePaths.ACACIA_BUSH, 2));
			ConfiguredFeatures.register(ctx, MARSH_BUSH, RTFFeatures.BUSH, makeSmallBush(Blocks.OAK_LOG, Blocks.BIRCH_LEAVES, 0.05F, 0.09F, 0.65F));
			ConfiguredFeatures.register(ctx, PLAINS_BUSH, RTFFeatures.BUSH, makeSmallBush(Blocks.OAK_LOG, Blocks.BIRCH_LEAVES, 0.05F, 0.09F, 0.65F));
			ConfiguredFeatures.register(ctx, STEPPE_BUSH, RTFFeatures.BUSH, makeSmallBush(Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES, 0.06F, 0.08F, 0.7F));
			ConfiguredFeatures.register(ctx, COLD_STEPPE_BUSH, RTFFeatures.BUSH, makeSmallBush(Blocks.SPRUCE_LOG, Blocks.OAK_LEAVES, 0.05F, 0.075F, 0.6F));
			ConfiguredFeatures.register(ctx, TAIGA_SCRUB_BUSH, RTFFeatures.BUSH, makeSmallBush(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, 0.05F, 0.075F, 0.6F));
			
			ConfiguredFeatures.register(ctx, PLAINS_TREES, Feature.RANDOM_SELECTOR, makeRandom(oakForest, List.of(
				makeWeighted(0.2F, oakForest), 
				makeWeighted(0.3F, oakLarge)
			)));
			ConfiguredFeatures.register(ctx, FOREST_TREES, Feature.RANDOM_SELECTOR, makeRandom(oakForest, List.of(
				makeWeighted(0.2F, oakForest), 
				makeWeighted(0.3F, oakLarge)
			)));
			ConfiguredFeatures.register(ctx, FLOWER_FOREST_TREES, Feature.RANDOM_SELECTOR, makeRandom(birchForest, List.of(
				makeWeighted(0.2F, birchForest), 
				makeWeighted(0.2F, birchLarge), 
				makeWeighted(0.2F, oakForest), 
				makeWeighted(0.2F, oakLarge)
			)));
			ConfiguredFeatures.register(ctx, BIRCH_TREES, RTFFeatures.CHANCE, makeChance(
				makeChanceEntry(birchLarge, 0.2F, RTFChanceModifiers.elevation(0.25F, 0.0F), RTFChanceModifiers.biomeEdge(0.1F, 0.3F)),
				makeChanceEntry(birchForest, 0.2F, RTFChanceModifiers.elevation(0.3F, 0.0F), RTFChanceModifiers.biomeEdge(0.05F, 0.2F)),
				makeChanceEntry(birchSmall, 0.1F, RTFChanceModifiers.biomeEdge(0.25F, 0.0F)),
				makeChanceEntry(birchSmall, 0.1F, RTFChanceModifiers.elevation(0.25F, 0.65F))
			));
			ConfiguredFeatures.register(ctx, DARK_FOREST_TREES, Feature.RANDOM_SELECTOR, makeRandom(darkOakLarge, List.of(
				makeWeighted(0.025F, hugeBrownMushroom),
				makeWeighted(0.05F, hugeRedMushroom),
				makeWeighted(0.3F, darkOakLarge),
				makeWeighted(0.2F, darkOakSmall),
				makeWeighted(0.05F, birchForest),
				makeWeighted(0.025F, oakForest)
			)));
			ConfiguredFeatures.register(ctx, SAVANNA_TREES, Feature.RANDOM_SELECTOR, makeRandom(acaciaLarge, List.of(
				makeWeighted(0.4F, acaciaLarge), 
				makeWeighted(0.15F, acaciaSmall)
			)));
			ConfiguredFeatures.register(ctx, BADLANDS_TREES, Feature.RANDOM_SELECTOR, makeRandom(oakSmall, List.of(
				makeWeighted(0.2F, oakSmall), 
				makeWeighted(0.1F, acaciaBush)
			)));
			ConfiguredFeatures.register(ctx, WOODED_BADLANDS_TREES, Feature.RANDOM_SELECTOR, makeRandom(oakSmall, List.of(
				makeWeighted(0.3F, oakSmall), 
				makeWeighted(0.2F, acaciaBush)
			)));
			ConfiguredFeatures.register(ctx, SWAMP_TREES, Feature.RANDOM_SELECTOR, makeRandom(willowLarge, List.of(
				makeWeighted(0.2F, willowSmall), 
				makeWeighted(0.35F, willowLarge)
			)));
			ConfiguredFeatures.register(ctx, MEADOW_TREES, Feature.RANDOM_SELECTOR, makeRandom(meadowNormal, List.of(
				makeWeighted(0.2F, meadowNormal), 
				makeWeighted(0.35F, meadowVariant)
			)));
			ConfiguredFeatures.register(ctx, FIR_TREES, RTFFeatures.CHANCE, makeChance(
				makeChanceEntry(spruceSmall, 0.1F, RTFChanceModifiers.elevation(0.55F, 0.2F)),
				makeChanceEntry(spruceLarge, 0.25F, RTFChanceModifiers.elevation(0.3F, 0.0F))
			));
			ConfiguredFeatures.register(ctx, GROVE_TREES, RTFFeatures.CHANCE, makeChance(
				makeChanceEntry(spruceSmallOnSnow, 0.1F, RTFChanceModifiers.elevation(1.0F, 0.2F)),
				makeChanceEntry(spruceLargeOnSnow, 0.25F, RTFChanceModifiers.elevation(0.3F, 0.0F))
			));
			
			ConfiguredFeatures.register(ctx, REDWOOD_TREES, RTFFeatures.CHANCE, makeChance(
				makeChanceEntry(redwoodHuge, 0.4F, RTFChanceModifiers.elevation(0.15F, 0.0F), RTFChanceModifiers.biomeEdge(0.1F, 0.3F)),
				makeChanceEntry(redwoodLarge, 0.2F, RTFChanceModifiers.elevation(0.25F, 0.0F), RTFChanceModifiers.biomeEdge(0.05F, 0.25F)),
				makeChanceEntry(spruceLarge, 0.4F, RTFChanceModifiers.elevation(0.35F, 0.15F)),
				makeChanceEntry(spruceSmall, 0.2F, RTFChanceModifiers.elevation(0.5F, 0.2F))
			));
			ConfiguredFeatures.register(ctx, JUNGLE_TREES, Feature.RANDOM_SELECTOR, makeRandom(jungleSmall, List.of(
				makeWeighted(0.2F, jungleSmall), 
				makeWeighted(0.3F, jungleLarge),
				makeWeighted(0.4F, jungleHuge),
				makeWeighted(0.5F, jungleBush)
			)));
			ConfiguredFeatures.register(ctx, JUNGLE_EDGE_TREES, Feature.RANDOM_SELECTOR, makeRandom(jungleSmall, List.of(
				makeWeighted(0.2F, jungleSmall), 
				makeWeighted(0.3F, jungleLarge),
				makeWeighted(0.4F, jungleBush)
			)));
			ConfiguredFeatures.register(ctx, FOREST_GRASS, Feature.RANDOM_SELECTOR, makeRandom(
				makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.GRASS, 48)),
				List.of(
					makeWeighted(0.5F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.GRASS, 56))),
					makeWeighted(0.4F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.TALL_GRASS, 56))),
					makeWeighted(0.2F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.LARGE_FERN, 48))),
					makeWeighted(0.2F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.FERN, 24)))
				)
			));
			ConfiguredFeatures.register(ctx, MEADOW_GRASS, Feature.RANDOM_SELECTOR, makeRandom(
				makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.FERN, 15)),
				List.of(
					makeWeighted(0.5F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.GRASS, 15))),
					makeWeighted(0.5F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.FERN, 36))),
					makeWeighted(0.2F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.LARGE_FERN, 55))),
					makeWeighted(0.4F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.TALL_GRASS, 45)))
				)
			));
			ConfiguredFeatures.register(ctx, FERN_GRASS, Feature.RANDOM_SELECTOR, makeRandom(
				makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.GRASS, 48)),
				List.of(
					makeWeighted(0.55F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.GRASS, 56))),
					makeWeighted(0.2F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.TALL_GRASS, 24))),
					makeWeighted(0.3F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.LARGE_FERN, 24))),
					makeWeighted(0.5F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.FERN, 36)))
				)
			));
			ConfiguredFeatures.register(ctx, BIRCH_GRASS, Feature.RANDOM_SELECTOR, makeRandom(
				makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.TALL_GRASS, 48)),
				List.of(
					makeWeighted(0.8F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.TALL_GRASS, 56))),
					makeWeighted(0.5F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.LILAC, 64))),
					makeWeighted(0.3F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.LARGE_FERN, 48))),
					makeWeighted(0.2F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.FERN, 24))),
					makeWeighted(0.1F, makeInlined(Feature.RANDOM_PATCH, makePatch(Blocks.PEONY, 32)))
				)
			));

	        ConfiguredFeatures.register(ctx, MiscConfiguredFeatures.DISK_CLAY, RTFFeatures.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.CLAY), BlockPredicate.matchingBlocks(List.of(Blocks.DIRT, Blocks.CLAY)), UniformIntProvider.create(2, 3), 1));
	        ConfiguredFeatures.register(ctx, MiscConfiguredFeatures.DISK_GRAVEL, RTFFeatures.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.GRAVEL), BlockPredicate.matchingBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)), UniformIntProvider.create(2, 5), 2));
	        ConfiguredFeatures.register(ctx, MiscConfiguredFeatures.DISK_SAND, RTFFeatures.DISK, new DiskFeatureConfig(new PredicatedStateProvider(BlockStateProvider.of(Blocks.SAND), List.of(new PredicatedStateProvider.Rule(BlockPredicate.matchingBlocks(Direction.DOWN.getVector(), Blocks.AIR), BlockStateProvider.of(Blocks.SANDSTONE)))), BlockPredicate.matchingBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)), UniformIntProvider.create(2, 6), 2));

	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.ACACIA, Feature.RANDOM_SELECTOR, makeRandom(acaciaSmall, List.of(
	        	makeWeighted(1.0F, acaciaSmall), 
	        	makeWeighted(0.5F, acaciaLarge)
			)));
	        RandomFeatureConfig birchConfig = makeRandom(birchSmall, List.of(
	        	makeWeighted(1.0F, birchSmall), 
	        	makeWeighted(0.5F, birchLarge)
			));
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.BIRCH, Feature.RANDOM_SELECTOR, birchConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.BIRCH_BEES_005, Feature.RANDOM_SELECTOR, birchConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.DARK_OAK, Feature.RANDOM_SELECTOR, makeRandom(darkOakSmall, List.of(
	        	makeWeighted(1.0F, darkOakSmall), 
	        	makeWeighted(0.5F, darkOakLarge)
			)));
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.JUNGLE_TREE_NO_VINE, RTFFeatures.TEMPLATE, jungleSmallConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.MEGA_JUNGLE_TREE, Feature.RANDOM_SELECTOR, makeRandom(jungleLarge, List.of(
	        	makeWeighted(1.0F, jungleLarge), 
	        	makeWeighted(0.5F, jungleHuge)
			)));
	        RandomFeatureConfig oakConfig = makeRandom(oakSmall, List.of(
	        	makeWeighted(1.0F, oakSmall), 
	        	makeWeighted(0.5F, oakLarge)
			));
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.OAK, Feature.RANDOM_SELECTOR, oakConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.OAK_BEES_005, Feature.RANDOM_SELECTOR, oakConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.FANCY_OAK_BEES_005, Feature.RANDOM_SELECTOR, oakConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.FANCY_OAK, Feature.RANDOM_SELECTOR, oakConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.SPRUCE, Feature.RANDOM_SELECTOR, makeRandom(spruceSmall, List.of(
	        	makeWeighted(1.0F, spruceSmall), 
	        	makeWeighted(0.5F, spruceLarge)
			)));
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.PINE, RTFFeatures.TEMPLATE, pineConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.MEGA_PINE, RTFFeatures.TEMPLATE, redwoodHugeConfig);
	        ConfiguredFeatures.register(ctx, TreeConfiguredFeatures.MEGA_SPRUCE, RTFFeatures.TEMPLATE, redwoodHugeConfig);
	    }
	}
	
	private static BushFeature.Config makeSmallBush(Block log, Block leaves, float air, float leaf, float size) {
		return new BushFeature.Config(log.getDefaultState(), leaves.getDefaultState(), air, leaf, size);
	}

	private static TemplateFeature.Config<?> makeTree(List<Identifier> templates) {
		return makeTree(templates, 3);
	}
		
	private static TemplateFeature.Config<?> makeTree(List<Identifier> templates, int baseExtension) {
		return makeTree(templates, TemplatePlacements.tree(), baseExtension);
	}
	
	private static TemplateFeature.Config<?> makeTree(List<Identifier> templates, TemplatePlacement<?> placement, int baseExtension) {
		return makeTree(templates, ImmutableList.of(), placement, baseExtension);
	}
	
	private static <T extends TemplateContext> TemplateFeature.Config<T> makeTree(List<Identifier> templates, List<TemplateDecorator<T>> decorators, TemplatePlacement<T> placement, int baseExtension) {
		return new TemplateFeature.Config<>(templates, placement, new PasteConfig(baseExtension, false, true, false, false), new DecoratorConfig<>(decorators, ImmutableMap.of()));
	}

	private static TemplateFeature.Config<TreeContext> makeTreeWithBehives(List<Identifier> templates, List<TemplateDecorator<TreeContext>> behives) {
		return makeTreeWithBehives(templates, behives, ImmutableMap.of());
	}
	
	private static TemplateFeature.Config<TreeContext> makeTreeWithBehives(List<Identifier> templates, List<TemplateDecorator<TreeContext>> behives, Map<RegistryKey<Biome>, List<TemplateDecorator<TreeContext>>> biomeOverrides) {
		return new TemplateFeature.Config<>(templates, TemplatePlacements.tree(), new PasteConfig(3, false, true, false, false), new DecoratorConfig<>(behives, biomeOverrides));
	}
	
	private static ChanceFeature.Config makeChance(ChanceFeature.Entry... entries) {
		return new ChanceFeature.Config(ImmutableList.copyOf(entries));
	}

	private static ChanceFeature.Entry makeChanceEntry(RegistryEntry<PlacedFeature> feature, float chance, ChanceModifier... modifiers) {
		return new ChanceFeature.Entry(feature, chance, ImmutableList.copyOf(modifiers));
	}
	
	private static RandomFeatureConfig makeRandom(RegistryEntry<PlacedFeature> defaultFeature, List<RandomFeatureEntry> entries) {
		return new RandomFeatureConfig(entries, defaultFeature);
	}

    private static RandomFeatureEntry makeWeighted(float weight, RegistryEntry<PlacedFeature> feature) {
    	return new RandomFeatureEntry(feature, weight);
    }
    
    private static RandomPatchFeatureConfig makePatch(Block state, int tries) {
        return ConfiguredFeatures.createRandomPatchFeatureConfig(tries, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(state.getDefaultState()))));
    }
    
	private static <FC extends FeatureConfig, F extends Feature<FC>> RegistryEntry<PlacedFeature> makeInlined(F feature, FC featureConfiguration) {
		return RegistryEntry.of(new PlacedFeature(RegistryEntry.of(new ConfiguredFeature<>(feature, featureConfiguration)), ImmutableList.of()));
	}

	protected static RegistryKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, RTFCommon.location(name));
	}
}
