package org.haberno.terraloomed.worldgen.surface.rule;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.haberno.terraloomed.platform.RegistryUtil;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.surface.rule.StrataRule.Layer;

import java.util.List;

public class RTFSurfaceRules {

	public static void bootstrap() {
		register("layered", LayeredSurfaceRule.CODEC);
		register("strata", StrataRule.CODEC);
		register("noise", NoiseRule.CODEC);
	}
	
	public static LayeredSurfaceRule layered(TagKey<LayeredSurfaceRule.Layer> layers) {
		return new LayeredSurfaceRule(layers);
	}
	
	public static StrataRule strata(Identifier cacheId, int buffer, int iterations, RegistryEntry<Noise> selector, List<Layer> layers) {
		return new StrataRule(cacheId, buffer, iterations, selector, layers);
	}
	
	public static NoiseRule noise(RegistryEntry<Noise> noise, List<Pair<Float, MaterialRules.MaterialRule>> rules) {
		return new NoiseRule(noise, rules);
	}
	
	public static void register(String name, Codec<? extends MaterialRules.MaterialRule> value) {
		RegistryUtil.register(Registries.MATERIAL_RULE, name, value);
	}
}
