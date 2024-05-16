package org.haberno.terraloomed.platform;

import java.util.concurrent.CompletableFuture;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.RegistryWrapper;

public final class DataGenUtil {

	@ExpectPlatform
	public static DataProvider createRegistryProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> providerLookup) {
		throw new IllegalStateException();
	}
}