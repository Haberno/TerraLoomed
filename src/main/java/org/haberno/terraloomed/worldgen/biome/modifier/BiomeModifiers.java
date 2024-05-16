package org.haberno.terraloomed.worldgen.biome.modifier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.haberno.terraloomed.platform.RegistryUtil;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;

import java.util.Map;
import java.util.Optional;

public class BiomeModifiers {

	public static void bootstrap() {
		register("add", AddModifier.CODEC);
		register("replace", ReplaceModifier.CODEC);
	}

	@SafeVarargs
	public static BiomeModifier add(Order order, GenerationStep.Feature step, RegistryEntry<PlacedFeature>... features) {
		return add(order, step, RegistryEntryList.of(features));
	}
	
	public static BiomeModifier add(Order order, GenerationStep.Feature step, RegistryEntryList<PlacedFeature> features) {
		return add(order, step, Optional.empty(), features);
	}

	@SafeVarargs
	public static BiomeModifier add(Order order, GenerationStep.Feature step, Filter.Behavior filterBehavior, RegistryEntryList<Biome> biomes, RegistryEntry<PlacedFeature>... features) {
		return add(order, step, filterBehavior, biomes, RegistryEntryList.of(features));
	}

	public static BiomeModifier add(Order order, GenerationStep.Feature step, Filter.Behavior filterBehavior, RegistryEntryList<Biome> biomes, RegistryEntryList<PlacedFeature> features) {
		return add(order, step, Optional.of(Pair.of(filterBehavior, biomes)), features);
	}
	
	public static BiomeModifier add(Order order, GenerationStep.Feature step, Optional<Pair<Filter.Behavior, RegistryEntryList<Biome>>> biomes, RegistryEntryList<PlacedFeature> features) {
		return new AddModifier(order, step, biomes.map((p) -> new Filter(p.getSecond(), p.getFirst())), features);
	}

	public static BiomeModifier replace(GenerationStep.Feature step, Map<RegistryKey<PlacedFeature>, RegistryEntry<PlacedFeature>> replacements) {
		return replace(step, Optional.empty(), replacements);
	}

	public static BiomeModifier replace(GenerationStep.Feature step, RegistryEntryList<Biome> biomes, Map<RegistryKey<PlacedFeature>, RegistryEntry<PlacedFeature>> replacements) {
		return replace(step, Optional.of(biomes), replacements);
	}
	
	public static BiomeModifier replace(GenerationStep.Feature step, Optional<RegistryEntryList<Biome>> biomes, Map<RegistryKey<PlacedFeature>, RegistryEntry<PlacedFeature>> replacements) {
		return new ReplaceModifier(step, biomes, replacements);
	}


	
	public static void register(String name, Codec<? extends BiomeModifier> value) {
		RegistryUtil.register(RTFBuiltInRegistries.BIOME_MODIFIER_TYPE, name, value);
	}
}
