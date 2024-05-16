package org.haberno.terraloomed.platform;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.haberno.terraloomed.registries.RTFRegistries;

@Deprecated
public final class RegistryUtil {
	
	public static <T> void register(Registry<T> registry, String name, T value) {
		getWritable(registry).add(RTFRegistries.createKey(registry.getKey(), name), value, Lifecycle.stable());
	}

	public static <T> MutableRegistry<T> getWritable(Registry<T> registry) {
		return (MutableRegistry<T>) registry;
	}
	
	public static <T> Registry<T> createRegistry(RegistryKey<? extends Registry<T>> key) {
		return FabricRegistryBuilder.createSimple((RegistryKey<Registry<T>>) key).buildAndRegister();
	}

	public static <T> void createDataRegistry(RegistryKey<? extends Registry<T>> key, Codec<T> codec) {
		DynamicRegistries.register(key, codec);
	}
}
