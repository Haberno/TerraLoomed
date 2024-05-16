package org.haberno.terraloomed.data.preset;

import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.MiscConfiguredFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.EnvironmentScanPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightmapPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.data.preset.settings.MiscellaneousSettings;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.world.worldgen.feature.placement.RTFPlacementModifiers;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.terrain.TerrainType;

public class PresetPlacedFeatures {
	public static final RegistryKey<PlacedFeature> ERODE_SNOW = createKey("erode_snow");
	public static final RegistryKey<PlacedFeature> SWAMP_SURFACE = createKey("swamp_surface");

	public static final RegistryKey<PlacedFeature> OAK_SMALL = createKey("oak/small");
	public static final RegistryKey<PlacedFeature> OAK_FOREST = createKey("oak/forest");
	public static final RegistryKey<PlacedFeature> OAK_LARGE = createKey("oak/large");
	public static final RegistryKey<PlacedFeature> BIRCH_SMALL = createKey("birch/small");
	public static final RegistryKey<PlacedFeature> BIRCH_FOREST = createKey("birch/forest");
	public static final RegistryKey<PlacedFeature> BIRCH_LARGE = createKey("birch/large");
	public static final RegistryKey<PlacedFeature> ACACIA_BUSH = createKey("acacia/bush");
	public static final RegistryKey<PlacedFeature> ACACIA_SMALL = createKey("acacia/small");
	public static final RegistryKey<PlacedFeature> ACACIA_LARGE = createKey("acacia/large");
	public static final RegistryKey<PlacedFeature> DARK_OAK_SMALL = createKey("dark_oak/small");
	public static final RegistryKey<PlacedFeature> DARK_OAK_LARGE = createKey("dark_oak/large");
	public static final RegistryKey<PlacedFeature> HUGE_BROWN_MUSHROOM = createKey("mushrooms/huge_brown_mushroom");
	public static final RegistryKey<PlacedFeature> HUGE_RED_MUSHROOM = createKey("mushrooms/huge_red_mushroom");
	public static final RegistryKey<PlacedFeature> WILLOW_SMALL = createKey("willow/small");
	public static final RegistryKey<PlacedFeature> WILLOW_LARGE = createKey("willow/large");
	public static final RegistryKey<PlacedFeature> MEADOW_NORMAL = createKey("meadow/normal");
	public static final RegistryKey<PlacedFeature> MEADOW_VARIANT = createKey("meadow/variant");
	public static final RegistryKey<PlacedFeature> PINE = createKey("pine/pine");
	public static final RegistryKey<PlacedFeature> SPRUCE_SMALL = createKey("spruce/small");
	public static final RegistryKey<PlacedFeature> SPRUCE_LARGE = createKey("spruce/large");
	public static final RegistryKey<PlacedFeature> SPRUCE_SMALL_ON_SNOW = createKey("spruce/small_on_snow");
	public static final RegistryKey<PlacedFeature> SPRUCE_LARGE_ON_SNOW = createKey("spruce/large_on_snow");
	public static final RegistryKey<PlacedFeature> REDWOOD_LARGE = createKey("redwood/large");
	public static final RegistryKey<PlacedFeature> REDWOOD_HUGE = createKey("redwood/huge");
	public static final RegistryKey<PlacedFeature> JUNGLE_SMALL = createKey("jungle/small");
	public static final RegistryKey<PlacedFeature> JUNGLE_LARGE = createKey("jungle/large");
	public static final RegistryKey<PlacedFeature> JUNGLE_HUGE = createKey("jungle/huge");

	public static final RegistryKey<PlacedFeature> MARSH_BUSH = createKey("shrubs/marsh_bush");
	public static final RegistryKey<PlacedFeature> PLAINS_BUSH = createKey("shrubs/plains_bush");
	public static final RegistryKey<PlacedFeature> STEPPE_BUSH = createKey("shrubs/steppe_bush");
	public static final RegistryKey<PlacedFeature> COLD_STEPPE_BUSH = createKey("shrubs/cold_steppe_bush");
	public static final RegistryKey<PlacedFeature> TAIGA_SCRUB_BUSH = createKey("shrubs/taiga_scrub_bush");

	public static final RegistryKey<PlacedFeature> FOREST_GRASS = createKey("forest_grass");
	public static final RegistryKey<PlacedFeature> MEADOW_GRASS = createKey("meadow_grass");
	public static final RegistryKey<PlacedFeature> FERN_GRASS = createKey("fern_grass");
	public static final RegistryKey<PlacedFeature> BIRCH_GRASS = createKey("birch_grass");
	
	public static final RegistryKey<PlacedFeature> PLAINS_TREES = createKey("plains_trees");
	public static final RegistryKey<PlacedFeature> FOREST_TREES = createKey("forest_trees");
	public static final RegistryKey<PlacedFeature> FLOWER_FOREST_TREES = createKey("flower_forest_trees");
	public static final RegistryKey<PlacedFeature> BIRCH_TREES = createKey("birch_trees");
	public static final RegistryKey<PlacedFeature> DARK_FOREST_TREES = createKey("dark_forest_trees");
	public static final RegistryKey<PlacedFeature> BADLANDS_TREES = createKey("badlands_trees");
	public static final RegistryKey<PlacedFeature> WOODED_BADLANDS_TREES = createKey("wooded_badlands_trees");
	public static final RegistryKey<PlacedFeature> SAVANNA_TREES = createKey("savanna_trees");
	public static final RegistryKey<PlacedFeature> SWAMP_TREES = createKey("swamp_trees");
	public static final RegistryKey<PlacedFeature> MEADOW_TREES = createKey("meadow_trees");
	public static final RegistryKey<PlacedFeature> FIR_TREES = createKey("fir_trees");
	public static final RegistryKey<PlacedFeature> GROVE_TREES = createKey("grove_trees");
	public static final RegistryKey<PlacedFeature> WINDSWEPT_HILLS_FIR_TREES = createKey("windswept_hills_fir_trees");
	public static final RegistryKey<PlacedFeature> PINE_TREES = createKey("pine_trees");
	public static final RegistryKey<PlacedFeature> SPRUCE_TREES = createKey("spruce_trees");
	public static final RegistryKey<PlacedFeature> SPRUCE_TUNDRA_TREES = createKey("spruce_tundra_trees");
	public static final RegistryKey<PlacedFeature> REDWOOD_TREES = createKey("redwood_trees");
	public static final RegistryKey<PlacedFeature> JUNGLE_TREES = createKey("jungle_trees");
	public static final RegistryKey<PlacedFeature> JUNGLE_EDGE_TREES = createKey("jungle_edge_trees");
    
	public static void bootstrap(Preset preset, Registerable<PlacedFeature> ctx) {
		RegistryEntryLookup<ConfiguredFeature<?, ?>> features = ctx.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
		RegistryEntryLookup<Noise> noises = ctx.getRegistryLookup(RTFRegistries.NOISE);
		
		MiscellaneousSettings miscellaneous = preset.miscellaneous();

		PlacementModifier blacklistOverworld = RTFPlacementModifiers.dimensionFilter(DimensionOptions.OVERWORLD);
		
		if(miscellaneous.naturalSnowDecorator || miscellaneous.smoothLayerDecorator) {
			PlacedFeatures.register(ctx, ERODE_SNOW, features.getOrThrow(PresetConfiguredFeatures.ERODE_SNOW));
		}
		
		PlacedFeatures.register(ctx, SWAMP_SURFACE, features.getOrThrow(PresetConfiguredFeatures.SWAMP_SURFACE));
		
        if(!miscellaneous.vanillaSprings) {
        	PlacedFeatures.register(ctx, MiscPlacedFeatures.SPRING_WATER, features.getOrThrow(MiscConfiguredFeatures.SPRING_WATER), blacklistOverworld);
        }
        
        if(!miscellaneous.vanillaLavaSprings) {
        	PlacedFeatures.register(ctx, MiscPlacedFeatures.SPRING_LAVA, features.getOrThrow(MiscConfiguredFeatures.SPRING_LAVA_OVERWORLD), blacklistOverworld);
        	PlacedFeatures.register(ctx, MiscPlacedFeatures.SPRING_LAVA_FROZEN, features.getOrThrow(MiscConfiguredFeatures.SPRING_LAVA_FROZEN), blacklistOverworld);
        }
        
        if(!miscellaneous.vanillaLavaLakes) {
            PlacedFeatures.register(ctx, MiscPlacedFeatures.LAKE_LAVA_SURFACE, features.getOrThrow(MiscConfiguredFeatures.LAKE_LAVA), blacklistOverworld);
            PlacedFeatures.register(ctx, MiscPlacedFeatures.LAKE_LAVA_UNDERGROUND, features.getOrThrow(MiscConfiguredFeatures.LAKE_LAVA), blacklistOverworld);
        }

        if(miscellaneous.strataDecorator) {
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_DIRT, features.getOrThrow(OreConfiguredFeatures.ORE_DIRT), blacklistOverworld);
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_GRAVEL, features.getOrThrow(OreConfiguredFeatures.ORE_GRAVEL), blacklistOverworld);
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_GRANITE_UPPER, features.getOrThrow(OreConfiguredFeatures.ORE_GRANITE), blacklistOverworld);
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_DIORITE_UPPER, features.getOrThrow(OreConfiguredFeatures.ORE_DIORITE), blacklistOverworld);
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_ANDESITE_UPPER, features.getOrThrow(OreConfiguredFeatures.ORE_ANDESITE), blacklistOverworld);
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_GRANITE_LOWER, features.getOrThrow(OreConfiguredFeatures.ORE_GRANITE), blacklistOverworld);
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_DIORITE_LOWER, features.getOrThrow(OreConfiguredFeatures.ORE_DIORITE), blacklistOverworld);
            PlacedFeatures.register(ctx, OrePlacedFeatures.ORE_ANDESITE_LOWER, features.getOrThrow(OreConfiguredFeatures.ORE_ANDESITE), blacklistOverworld);
        }
        
        if(miscellaneous.customBiomeFeatures) {
        	PlacedFeatures.register(ctx, OAK_SMALL, features.getOrThrow(PresetConfiguredFeatures.OAK_SMALL), PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
        	PlacedFeatures.register(ctx, OAK_FOREST, features.getOrThrow(PresetConfiguredFeatures.OAK_FOREST), PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
            PlacedFeatures.register(ctx, OAK_LARGE, features.getOrThrow(PresetConfiguredFeatures.OAK_LARGE), PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
            PlacedFeatures.register(ctx, BIRCH_SMALL, features.getOrThrow(PresetConfiguredFeatures.BIRCH_SMALL), PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING));
            PlacedFeatures.register(ctx, BIRCH_FOREST, features.getOrThrow(PresetConfiguredFeatures.BIRCH_FOREST), PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING));
            PlacedFeatures.register(ctx, BIRCH_LARGE, features.getOrThrow(PresetConfiguredFeatures.BIRCH_LARGE), PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING));
            PlacedFeatures.register(ctx, ACACIA_SMALL, features.getOrThrow(PresetConfiguredFeatures.ACACIA_SMALL), PlacedFeatures.wouldSurvive(Blocks.ACACIA_SAPLING));
            PlacedFeatures.register(ctx, ACACIA_LARGE, features.getOrThrow(PresetConfiguredFeatures.ACACIA_LARGE), PlacedFeatures.wouldSurvive(Blocks.ACACIA_SAPLING));
            PlacedFeatures.register(ctx, DARK_OAK_SMALL, features.getOrThrow(PresetConfiguredFeatures.DARK_OAK_SMALL), PlacedFeatures.wouldSurvive(Blocks.DARK_OAK_SAPLING));
            PlacedFeatures.register(ctx, DARK_OAK_LARGE, features.getOrThrow(PresetConfiguredFeatures.DARK_OAK_LARGE), PlacedFeatures.wouldSurvive(Blocks.DARK_OAK_SAPLING));
            PlacedFeatures.register(ctx, HUGE_BROWN_MUSHROOM, features.getOrThrow(PresetConfiguredFeatures.HUGE_BROWN_MUSHROOM));
            PlacedFeatures.register(ctx, HUGE_RED_MUSHROOM, features.getOrThrow(PresetConfiguredFeatures.HUGE_RED_MUSHROOM));
            PlacedFeatures.register(ctx, WILLOW_SMALL, features.getOrThrow(PresetConfiguredFeatures.WILLOW_SMALL), PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
            PlacedFeatures.register(ctx, WILLOW_LARGE, features.getOrThrow(PresetConfiguredFeatures.WILLOW_LARGE), PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
            PlacedFeatures.register(ctx, MEADOW_NORMAL, features.getOrThrow(PresetConfiguredFeatures.MEADOW_NORMAL), PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
            PlacedFeatures.register(ctx, MEADOW_VARIANT, features.getOrThrow(PresetConfiguredFeatures.MEADOW_VARIANT), PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING));
            PlacedFeatures.register(ctx, PINE, features.getOrThrow(PresetConfiguredFeatures.PINE), PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING));
            PlacedFeatures.register(ctx, SPRUCE_SMALL, features.getOrThrow(PresetConfiguredFeatures.SPRUCE_SMALL), PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING));
            PlacedFeatures.register(ctx, SPRUCE_LARGE, features.getOrThrow(PresetConfiguredFeatures.SPRUCE_LARGE), PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING));

            BlockPredicate isSnowPredicate = BlockPredicate.matchingBlocks(Direction.DOWN.getVector(), Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW);
            List<PlacementModifier> onSnowPlacement = List.of(EnvironmentScanPlacementModifier.of(Direction.UP, BlockPredicate.not(BlockPredicate.matchingBlocks(Blocks.POWDER_SNOW)), 8), BlockFilterPlacementModifier.of(isSnowPredicate));
            PlacedFeatures.register(ctx, SPRUCE_SMALL_ON_SNOW, features.getOrThrow(PresetConfiguredFeatures.SPRUCE_SMALL_ON_SNOW), onSnowPlacement);
            PlacedFeatures.register(ctx, SPRUCE_LARGE_ON_SNOW, features.getOrThrow(PresetConfiguredFeatures.SPRUCE_LARGE_ON_SNOW), onSnowPlacement);
            PlacedFeatures.register(ctx, REDWOOD_LARGE, features.getOrThrow(PresetConfiguredFeatures.REDWOOD_LARGE), PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING));
            PlacedFeatures.register(ctx, REDWOOD_HUGE, features.getOrThrow(PresetConfiguredFeatures.REDWOOD_HUGE), PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING));
            PlacedFeatures.register(ctx, JUNGLE_SMALL, features.getOrThrow(PresetConfiguredFeatures.JUNGLE_SMALL), PlacedFeatures.wouldSurvive(Blocks.JUNGLE_SAPLING));
            PlacedFeatures.register(ctx, JUNGLE_LARGE, features.getOrThrow(PresetConfiguredFeatures.JUNGLE_LARGE), PlacedFeatures.wouldSurvive(Blocks.JUNGLE_SAPLING));
            PlacedFeatures.register(ctx, JUNGLE_HUGE, features.getOrThrow(PresetConfiguredFeatures.JUNGLE_HUGE), PlacedFeatures.wouldSurvive(Blocks.JUNGLE_SAPLING));
            
            PlacedFeatures.register(ctx, ACACIA_BUSH, features.getOrThrow(PresetConfiguredFeatures.ACACIA_BUSH), PlacedFeatures.wouldSurvive(Blocks.ACACIA_SAPLING));
        	PlacedFeatures.register(ctx, MARSH_BUSH, features.getOrThrow(PresetConfiguredFeatures.MARSH_BUSH), RTFPlacementModifiers.countExtra(0, 0.3F, 1), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, PLAINS_BUSH, features.getOrThrow(PresetConfiguredFeatures.PLAINS_BUSH), RTFPlacementModifiers.countExtra(0, 0.05F, 1), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, STEPPE_BUSH, features.getOrThrow(PresetConfiguredFeatures.STEPPE_BUSH), RTFPlacementModifiers.countExtra(0, 0.125F, 1), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), PlacedFeatures.wouldSurvive(Blocks.ACACIA_SAPLING), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, COLD_STEPPE_BUSH, features.getOrThrow(PresetConfiguredFeatures.COLD_STEPPE_BUSH), RTFPlacementModifiers.countExtra(0, 0.125F, 1), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, TAIGA_SCRUB_BUSH, features.getOrThrow(PresetConfiguredFeatures.TAIGA_SCRUB_BUSH), RTFPlacementModifiers.countExtra(0, 0.1F, 1), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING), BiomePlacementModifier.of());
            
            PlacedFeatures.register(ctx, FOREST_GRASS, features.getOrThrow(PresetConfiguredFeatures.FOREST_GRASS), worldSurfaceSquaredWithCount(7));
            PlacedFeatures.register(ctx, MEADOW_GRASS, features.getOrThrow(PresetConfiguredFeatures.MEADOW_GRASS), worldSurfaceSquaredWithCount(7));
            PlacedFeatures.register(ctx, FERN_GRASS, features.getOrThrow(PresetConfiguredFeatures.FERN_GRASS), worldSurfaceSquaredWithCount(7));
            PlacedFeatures.register(ctx, BIRCH_GRASS, features.getOrThrow(PresetConfiguredFeatures.BIRCH_GRASS), worldSurfaceSquaredWithCount(7));
            
            PlacedFeatures.register(ctx, PLAINS_TREES, features.getOrThrow(PresetConfiguredFeatures.PLAINS_TREES), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, RTFPlacementModifiers.countExtra(0, 0.02F, 1), BiomePlacementModifier.of());
          	PlacedFeatures.register(ctx, FOREST_TREES, features.getOrThrow(PresetConfiguredFeatures.FOREST_TREES), RTFPlacementModifiers.poisson(7, 0.25F, 0.3F, 150, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
          	PlacedFeatures.register(ctx, FLOWER_FOREST_TREES, features.getOrThrow(PresetConfiguredFeatures.FLOWER_FOREST_TREES), RTFPlacementModifiers.poisson(8, 0.2F, 0.1F, 500, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
          	PlacedFeatures.register(ctx, BIRCH_TREES, features.getOrThrow(PresetConfiguredFeatures.BIRCH_TREES), RTFPlacementModifiers.poisson(6, 0.25F, 0.25F, 175, 0.9F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, DARK_FOREST_TREES, features.getOrThrow(PresetConfiguredFeatures.DARK_FOREST_TREES), RTFPlacementModifiers.poisson(5, 0.3F, 0.2F, 300, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, SAVANNA_TREES, features.getOrThrow(PresetConfiguredFeatures.SAVANNA_TREES), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, RTFPlacementModifiers.countExtra(0, 0.1F, 1), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, BADLANDS_TREES, features.getOrThrow(PresetConfiguredFeatures.BADLANDS_TREES), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, RTFPlacementModifiers.countExtra(0, 0.02F, 3), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, WOODED_BADLANDS_TREES, features.getOrThrow(PresetConfiguredFeatures.WOODED_BADLANDS_TREES), RTFPlacementModifiers.poisson(8, 0.2F, 0.8F, 0.25F, 150, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, SWAMP_TREES, features.getOrThrow(PresetConfiguredFeatures.SWAMP_TREES), RTFPlacementModifiers.poisson(6, 0.75F, 0.4F, 250, 0.0F), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, MEADOW_TREES, features.getOrThrow(PresetConfiguredFeatures.MEADOW_TREES), RTFPlacementModifiers.terrainFilter(true, TerrainType.DALES), PlacedFeatures.createCountExtraModifier(12, 0.5F, 3), SquarePlacementModifier.of(), RTFPlacementModifiers.noiseFilter(noises.getOrThrow(PresetFeatureNoise.MEADOW_TREES), 0.6F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, FIR_TREES, features.getOrThrow(PresetConfiguredFeatures.FIR_TREES), RTFPlacementModifiers.poisson(4, 0.25F, 0.3F, 300, 0.6F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, GROVE_TREES, features.getOrThrow(PresetConfiguredFeatures.GROVE_TREES), RTFPlacementModifiers.poisson(4, 0.25F, 0.3F, 300, 0.6F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, WINDSWEPT_HILLS_FIR_TREES, features.getOrThrow(PresetConfiguredFeatures.FIR_TREES), RarityFilterPlacementModifier.of(30), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, PINE_TREES, features.getOrThrow(PresetConfiguredFeatures.PINE), RTFPlacementModifiers.poisson(7, 0.25F, 0.25F, 250, 0.7F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, SPRUCE_TREES, features.getOrThrow(PresetConfiguredFeatures.PINE), RTFPlacementModifiers.poisson(7, 0.3F, 0.25F, 250, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, SPRUCE_TUNDRA_TREES, features.getOrThrow(PresetConfiguredFeatures.PINE), RarityFilterPlacementModifier.of(80), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, REDWOOD_TREES, features.getOrThrow(PresetConfiguredFeatures.REDWOOD_TREES), RTFPlacementModifiers.poisson(6, 0.3F, 0.25F, 250, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, JUNGLE_TREES, features.getOrThrow(PresetConfiguredFeatures.JUNGLE_TREES), RTFPlacementModifiers.poisson(6, 0.4F, 0.2F, 400, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        	PlacedFeatures.register(ctx, JUNGLE_EDGE_TREES, features.getOrThrow(PresetConfiguredFeatures.JUNGLE_EDGE_TREES), RTFPlacementModifiers.poisson(8, 0.35F, 0.25F, 350, 0.75F), HeightmapPlacementModifier.of(Type.WORLD_SURFACE), BiomePlacementModifier.of());
        }
	}
	
    public static List<PlacementModifier> worldSurfaceSquaredWithCount(int i) {
        return List.of(CountPlacementModifier.of(i), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
    }
	
    private static RegistryKey<PlacedFeature> createKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, RTFCommon.location(name));
    }
}
