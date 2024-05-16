package org.haberno.terraloomed;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.haberno.terraloomed.compat.terrablender.TBCompat;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.platform.RegistryUtil;
import org.haberno.terraloomed.registries.RTFArgumentTypeInfos;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.server.commands.RTFCommands;
import org.haberno.terraloomed.worldgen.biome.modifier.BiomeModifier;
import org.haberno.terraloomed.worldgen.biome.modifier.BiomeModifiers;
import org.haberno.terraloomed.worldgen.densityfunction.RTFDensityFunctions;
import org.haberno.terraloomed.worldgen.feature.RTFFeatures;
import org.haberno.terraloomed.worldgen.feature.chance.RTFChanceModifiers;
import org.haberno.terraloomed.worldgen.feature.placement.RTFPlacementModifiers;
import org.haberno.terraloomed.worldgen.feature.template.decorator.TemplateDecorators;
import org.haberno.terraloomed.worldgen.feature.template.placement.TemplatePlacements;
import org.haberno.terraloomed.worldgen.floatproviders.RTFFloatProviderTypes;
import org.haberno.terraloomed.worldgen.heightproviders.RTFHeightProviderTypes;
import org.haberno.terraloomed.worldgen.noise.domain.Domains;
import org.haberno.terraloomed.worldgen.noise.function.CurveFunctions;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.noise.module.Noises;
import org.haberno.terraloomed.worldgen.structure.rule.StructureRule;
import org.haberno.terraloomed.worldgen.structure.rule.StructureRules;
import org.haberno.terraloomed.worldgen.surface.condition.RTFSurfaceConditions;
import org.haberno.terraloomed.worldgen.surface.rule.LayeredSurfaceRule;
import org.haberno.terraloomed.worldgen.surface.rule.RTFSurfaceRules;

public class RTFCommon {
	public static final String MOD_ID = "reterraforged";
	public static final String LEGACY_MOD_ID = "terraforged";
	public static final Logger LOGGER = LogManager.getLogger("ReTerraForged");

	public static void bootstrap() {
		RTFBuiltInRegistries.bootstrap();
		TemplatePlacements.bootstrap();
		TemplateDecorators.bootstrap();
		RTFChanceModifiers.bootstrap();
		RTFPlacementModifiers.bootstrap();
		RTFDensityFunctions.bootstrap();
		Noises.bootstrap();
		Domains.bootstrap();
		CurveFunctions.bootstrap();
		RTFFeatures.bootstrap();
		RTFHeightProviderTypes.bootstrap();
		RTFFloatProviderTypes.bootstrap();
		RTFSurfaceRules.bootstrap();
		RTFSurfaceConditions.bootstrap();
		BiomeModifiers.bootstrap();
		StructureRules.bootstrap();

		RTFCommands.bootstrap();
		RTFArgumentTypeInfos.bootstrap();
		
		if(TBCompat.isEnabled()) {
			TBCompat.bootstrap();
		}
		
		RegistryUtil.createDataRegistry(RTFRegistries.NOISE, Noise.DIRECT_CODEC);
		RegistryUtil.createDataRegistry(RTFRegistries.BIOME_MODIFIER, BiomeModifier.CODEC);
		RegistryUtil.createDataRegistry(RTFRegistries.STRUCTURE_RULE, StructureRule.CODEC);
		RegistryUtil.createDataRegistry(RTFRegistries.SURFACE_LAYERS, LayeredSurfaceRule.Layer.CODEC);
		RegistryUtil.createDataRegistry(RTFRegistries.PRESET, Preset.CODEC);
	}
	
	public static Identifier location(String name) {
		if (name.contains(":")) return new Identifier(name);
		return new Identifier(RTFCommon.MOD_ID, name);
	}
}
