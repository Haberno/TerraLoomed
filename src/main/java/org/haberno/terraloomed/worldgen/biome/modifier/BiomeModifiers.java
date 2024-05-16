package org.haberno.terraloomed.worldgen.biome.modifier;

import java.util.Map;
import java.util.Optional;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import dev.architectury.injectables.annotations.ExpectPlatform;
import raccoonman.reterraforged.platform.RegistryUtil;
import raccoonman.reterraforged.registries.RTFBuiltInRegistries;

public class BiomeModifiers {

	@ExpectPlatform
	public static void bootstrap() {
		throw new UnsupportedOperationException();
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
	
	@ExpectPlatform
	public static BiomeModifier add(Order order, GenerationStep.Feature step, Optional<Pair<Filter.Behavior, RegistryEntryList<Biome>>> biomes, RegistryEntryList<PlacedFeature> features) {
		throw new UnsupportedOperationException();
	}

	public static BiomeModifier replace(GenerationStep.Feature step, Map<RegistryKey<PlacedFeature>, RegistryEntry<PlacedFeature>> replacements) {
		return replace(step, Optional.empty(), replacements);
	}

	public static BiomeModifier replace(GenerationStep.Feature step, RegistryEntryList<Biome> biomes, Map<RegistryKey<PlacedFeature>, RegistryEntry<PlacedFeature>> replacements) {
		return replace(step, Optional.of(biomes), replacements);
	}
	
	@ExpectPlatform
	public static BiomeModifier replace(GenerationStep.Feature step, Optional<RegistryEntryList<Biome>> biomes, Map<RegistryKey<PlacedFeature>, RegistryEntry<PlacedFeature>> replacements) {
		throw new UnsupportedOperationException();
	}
	
	public static void register(String name, Codec<? extends BiomeModifier> value) {
		RegistryUtil.register(RTFBuiltInRegistries.BIOME_MODIFIER_TYPE, name, value);
	}
}
