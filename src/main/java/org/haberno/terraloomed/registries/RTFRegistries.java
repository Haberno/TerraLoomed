package org.haberno.terraloomed.registries;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.haberno.terraloomed.RTFCommon;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.worldgen.biome.modifier.BiomeModifier;
import org.haberno.terraloomed.worldgen.feature.chance.ChanceModifier;
import org.haberno.terraloomed.worldgen.feature.template.decorator.TemplateDecorator;
import org.haberno.terraloomed.worldgen.feature.template.placement.TemplatePlacement;
import org.haberno.terraloomed.worldgen.noise.domain.Domain;
import org.haberno.terraloomed.worldgen.noise.function.CurveFunction;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.structure.rule.StructureRule;
import org.haberno.terraloomed.worldgen.surface.rule.LayeredSurfaceRule;

public class RTFRegistries {
	public static final RegistryKey<Registry<Codec<? extends Noise>>> NOISE_TYPE = createKey("worldgen/noise_type");
	public static final RegistryKey<Registry<Codec<? extends Domain>>> DOMAIN_TYPE = createKey("worldgen/domain_type");
	public static final RegistryKey<Registry<Codec<? extends CurveFunction>>> CURVE_FUNCTION_TYPE = createKey("worldgen/curve_function_type");
	public static final RegistryKey<Registry<Codec<? extends ChanceModifier>>> CHANCE_MODIFIER_TYPE = createKey("worldgen/chance_modifier_type");
	public static final RegistryKey<Registry<Codec<? extends TemplatePlacement<?>>>> TEMPLATE_PLACEMENT_TYPE = createKey("worldgen/template_placement_type");
	public static final RegistryKey<Registry<Codec<? extends TemplateDecorator<?>>>> TEMPLATE_DECORATOR_TYPE = createKey("worldgen/template_decorator_type");
	public static final RegistryKey<Registry<Codec<? extends BiomeModifier>>> BIOME_MODIFIER_TYPE = createKey("worldgen/biome_modifier_type");
	public static final RegistryKey<Registry<Codec<? extends StructureRule>>> STRUCTURE_RULE_TYPE = createKey("worldgen/structure_rule_type");
	public static final RegistryKey<Registry<Noise>> NOISE = createKey("worldgen/noise");
	public static final RegistryKey<Registry<BiomeModifier>> BIOME_MODIFIER = createKey("worldgen/biome_modifier");
	public static final RegistryKey<Registry<StructureRule>> STRUCTURE_RULE = createKey("worldgen/structure_rule");
	public static final RegistryKey<Registry<LayeredSurfaceRule.Layer>> SURFACE_LAYERS = createKey("worldgen/surface_layers");

	public static final RegistryKey<Registry<Preset>> PRESET = createKey("worldgen/preset");
	
	public static <T> RegistryKey<T> createKey(RegistryKey<? extends Registry<T>> registryKey, String valueKey) {
		return RegistryKey.of(registryKey, RTFCommon.location(valueKey));
	}

	private static <T> RegistryKey<Registry<T>> createKey(String key) {
		return RegistryKey.ofRegistry(RTFCommon.location(key));
	}
}
