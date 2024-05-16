package org.haberno.terraloomed.worldgen.feature;

import net.minecraft.registry.Registries;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import org.haberno.terraloomed.platform.RegistryUtil;
import org.haberno.terraloomed.worldgen.feature.chance.ChanceFeature;
import org.haberno.terraloomed.worldgen.feature.template.TemplateFeature;

public class RTFFeatures {
	public static final Feature<TemplateFeature.Config<?>> TEMPLATE = register("template", new TemplateFeature(TemplateFeature.Config.CODEC));
	public static final Feature<BushFeature.Config> BUSH = register("bush", new BushFeature(BushFeature.Config.CODEC));
	public static final Feature<DiskFeatureConfig> DISK = register("disk", new DiskFeature(DiskFeatureConfig.CODEC));
	public static final Feature<ChanceFeature.Config> CHANCE = register("chance", new ChanceFeature(ChanceFeature.Config.CODEC));
	public static final Feature<ErodeSnowFeature.Config> ERODE_SNOW = register("erode_snow", new ErodeSnowFeature(ErodeSnowFeature.Config.CODEC));
	public static final Feature<SwampSurfaceFeature.Config> SWAMP_SURFACE = register("swamp_surface", new SwampSurfaceFeature(SwampSurfaceFeature.Config.CODEC));
	
	public static void bootstrap() {
	}
	
	private static <T extends FeatureConfig> Feature<T> register(String name, Feature<T> feature) {
		RegistryUtil.register(Registries.FEATURE, name, feature);
		return feature;
	}
}
