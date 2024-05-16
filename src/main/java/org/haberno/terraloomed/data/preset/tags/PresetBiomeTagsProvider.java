package org.haberno.terraloomed.data.preset.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.haberno.terraloomed.data.preset.settings.MiscellaneousSettings;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.tags.RTFBiomeTags;

import java.util.concurrent.CompletableFuture;

public class PresetBiomeTagsProvider extends TagProvider<Biome> {
	private Preset preset;
	
	public PresetBiomeTagsProvider(Preset preset, DataOutput packOutput, CompletableFuture<WrapperLookup> completableFuture) {
		super(packOutput, RegistryKeys.BIOME, completableFuture);
		
		this.preset = preset;
	}

	@Override
	protected void configure(WrapperLookup provider) {
		MiscellaneousSettings miscellaneous = this.preset.miscellaneous();

		this.getOrCreateTagBuilder(RTFBiomeTags.HAS_SWAMP_SURFACE).add(BiomeKeys.SWAMP);
		this.getOrCreateTagBuilder(RTFBiomeTags.EROSION_BLACKLIST).add(BiomeKeys.DESERT, BiomeKeys.BADLANDS, BiomeKeys.WOODED_BADLANDS);
		
		if(miscellaneous.customBiomeFeatures) {
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_SWAMP_TREES).add(BiomeKeys.SWAMP);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_PLAINS_TREES).add(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.RIVER, BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.OCEAN, BiomeKeys.DEEP_OCEAN, BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.WARM_OCEAN);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_FOREST_TREES).add(BiomeKeys.FOREST);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_FLOWER_FOREST_TREES).add(BiomeKeys.FLOWER_FOREST);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_BIRCH_FOREST_TREES).add(BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_DARK_FOREST_TREES).add(BiomeKeys.DARK_FOREST);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_SAVANNA_TREES).add(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_MEADOW_TREES).add(BiomeKeys.MEADOW);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_FIR_FOREST_TREES).add(BiomeKeys.WINDSWEPT_FOREST);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_GROVE_TREES).add(BiomeKeys.GROVE);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_WINDSWEPT_HILLS_TREES).add(BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_PINE_FOREST_TREES).add(BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_SPRUCE_FOREST_TREES).add(BiomeKeys.SNOWY_TAIGA);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_SPRUCE_TUNDRA_TREES).add(BiomeKeys.SNOWY_PLAINS, BiomeKeys.FROZEN_RIVER);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_REDWOOD_FOREST_TREES).add(BiomeKeys.OLD_GROWTH_PINE_TAIGA);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_JUNGLE_TREES).add(BiomeKeys.JUNGLE, BiomeKeys.BAMBOO_JUNGLE);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_JUNGLE_EDGE_TREES).add(BiomeKeys.SPARSE_JUNGLE);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_BADLANDS_TREES).add(BiomeKeys.WINDSWEPT_SAVANNA);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_WOODED_BADLANDS_TREES).add(BiomeKeys.WOODED_BADLANDS);
	
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_MARSH_BUSHES).add();
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_PLAINS_BUSHES).add(BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.MEADOW);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_STEPPE_BUSHES).add(BiomeKeys.SAVANNA, BiomeKeys.WINDSWEPT_SAVANNA, BiomeKeys.SAVANNA_PLATEAU);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_COLD_STEPPE_BUSHES).add();
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_COLD_TAIGA_SCRUB_BUSHES).add(BiomeKeys.SNOWY_PLAINS, BiomeKeys.TAIGA, BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS);
			
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_FOREST_GRASS).add(BiomeKeys.FOREST, BiomeKeys.DARK_FOREST);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_MEADOW_GRASS).add(BiomeKeys.MEADOW, BiomeKeys.FLOWER_FOREST);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_FERN_GRASS).add(BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.TAIGA);
			this.getOrCreateTagBuilder(RTFBiomeTags.HAS_BIRCH_GRASS).add(BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
		}
	}
}