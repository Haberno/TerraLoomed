package org.haberno.terraloomed.platform;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.world.worldgen.biome.modifier.BiomeModifier;

@Deprecated
public final class RegistryUtil {
	
	public static <T> void register(Registry<T> registry, String name, T value) {
		getWritable(registry).add(RTFRegistries.createKey(registry.getKey(), name), value, Lifecycle.stable());
	}
	
	@ExpectPlatform
	public static Registry<BiomeModifier> getBiomeModifierRegistry() {
		throw new IllegalStateException();
	}
	
	@ExpectPlatform
	public static <T> MutableRegistry<T> getWritable(Registry<T> registry) {
		throw new IllegalStateException();
	}
	
	@ExpectPlatform
	public static <T> Registry<T> createRegistry(RegistryKey<? extends Registry<T>> key) {
		throw new IllegalStateException();
	}

	@ExpectPlatform
	public static <T> void createDataRegistry(RegistryKey<? extends Registry<T>> key, Codec<T> codec) {
		throw new IllegalStateException();
	}
}
