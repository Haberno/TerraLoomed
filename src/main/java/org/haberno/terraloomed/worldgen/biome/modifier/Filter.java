package org.haberno.terraloomed.worldgen.biome.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.biome.Biome;

public record Filter(RegistryEntryList<Biome> biomes, Behavior behavior) {
	public static final Codec<Filter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Biome.REGISTRY_ENTRY_LIST_CODEC.fieldOf("biomes").forGetter(Filter::biomes),
		Behavior.CODEC.fieldOf("behavior").forGetter(Filter::behavior)
	).apply(instance, Filter::new));
	
	public boolean test(RegistryEntry<Biome> biome) {
		return this.behavior.test(this.biomes, biome);
	}
	
	public enum Behavior implements StringIdentifiable {
		WHITELIST("whitelist") {
			
			@Override
			public boolean test(RegistryEntryList<Biome> biomes, RegistryEntry<Biome> biome) {
				return biomes.contains(biome);
			}
		},
		BLACKLIST("blacklist") {

			@Override
			public boolean test(RegistryEntryList<Biome> biomes, RegistryEntry<Biome> biome) {
				return !biomes.contains(biome);
			}
		};
		
		public static final Codec<Behavior> CODEC = StringIdentifiable.createCodec(Behavior::values);
		
		private String name;
		
		private Behavior(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return this.name;
		}
		
		public abstract boolean test(RegistryEntryList<Biome> biomes, RegistryEntry<Biome> biome);
	}
}
