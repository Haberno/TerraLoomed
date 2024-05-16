package org.haberno.terraloomed.platform;


import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import net.minecraft.data.DataOutput;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.*;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.data.DataProvider;

public class DataGenUtil {

	public static DataProvider createRegistryProvider(DataOutput output, CompletableFuture<WrapperLookup> providerLookup) {
		return new Provider(output, providerLookup);
	}

	// a port of the forge patches to RegistriesDatapackGenerator
	private static class Provider implements DataProvider {
		private static final Logger LOGGER = LogUtils.getLogger();
		private final DataOutput output;
		private final CompletableFuture<WrapperLookup> registries;

		public Provider(DataOutput arg, CompletableFuture<WrapperLookup> completableFuture) {
			this.registries = completableFuture;
			this.output = arg;
		}

		@Override
		public CompletableFuture<?> run(DataWriter arg) {
			return this.registries.thenCompose(provider -> {
				RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, provider);
				return CompletableFuture.allOf(DynamicRegistries.getDynamicRegistries().stream().flatMap(arg3 -> this.dumpRegistryCap(arg, provider, ops, arg3).stream()).toArray(CompletableFuture[]::new));
			});
		}

		private <T> Optional<CompletableFuture<?>> dumpRegistryCap(DataWriter output, WrapperLookup provider, DynamicOps<JsonElement> ops, RegistryLoader.Entry<T> registryData) {
			RegistryKey<? extends Registry<T>> key = registryData.key();

			return provider.getOptionalWrapper(key).map(lookup -> {
				DataOutput.PathResolver path = this.output.getResolver(DataOutput.OutputType.DATA_PACK, prefixNamespace(key.getRegistry()));
				Stream<RegistryEntry.Reference<T>> holders = lookup.streamEntries();
				return CompletableFuture.allOf(holders.map((ref) -> {
					return dumpValue(path.resolveJson(ref.registryKey().getValue()), output, ops, registryData.elementCodec(), ref.value());
				}).toArray(CompletableFuture[]::new));
			});
		}

		private static String prefixNamespace(Identifier location) {
			return location.getNamespace().equals("minecraft") ? location.getPath() : location.getNamespace() +  "/"  + location.getPath();
		}

		private static <E> CompletableFuture<?> dumpValue(Path path, DataWriter arg, DynamicOps<JsonElement> dynamicOps, Encoder<E> encoder, E object) {
			Optional<JsonElement> optional = encoder.encodeStart(dynamicOps, object).resultOrPartial(string -> LOGGER.error("Couldn't serialize element {}: {}", path, string));
			return optional.isPresent() ? DataProvider.writeToPath(arg, optional.get(), path) : CompletableFuture.completedFuture(null);
		}

		@Override
		public String getName() {
			return "Registries";
		}
	}
}