package org.haberno.terraloomed.worldgen.floatproviders;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.floatprovider.FloatProviderType;
import raccoonman.reterraforged.platform.RegistryUtil;

public class RTFFloatProviderTypes {
	public static final FloatProviderType<LegacyCanyonYScale> LEGACY_CANYON_Y_SCALE = register("legacy_canyon_y_scale", LegacyCanyonYScale.VALUE_CODEC);
	
	public static void bootstrap() {
	}
	
	private static <T extends FloatProvider> FloatProviderType<T> register(String name, Codec<T> codec) {
		FloatProviderType<T> type = () -> codec;
		RegistryUtil.register(Registries.FLOAT_PROVIDER_TYPE, name, type);
		return type;
	}
}
