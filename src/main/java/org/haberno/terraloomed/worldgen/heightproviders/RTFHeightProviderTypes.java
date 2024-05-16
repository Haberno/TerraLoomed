package org.haberno.terraloomed.worldgen.heightproviders;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.heightprovider.HeightProviderType;
import org.haberno.terraloomed.platform.RegistryUtil;

public class RTFHeightProviderTypes {
	public static final HeightProviderType<LegacyCarverHeight> LEGACY_CARVER = register("legacy_carver", LegacyCarverHeight.CODEC);
	
	public static void bootstrap() {
	}
	
	private static <T extends HeightProvider> HeightProviderType<T> register(String name, Codec<T> codec) {
		HeightProviderType<T> type = () -> codec;
		RegistryUtil.register(Registries.HEIGHT_PROVIDER_TYPE, name, type);
		return type;
	}
}
