package org.haberno.terraloomed.worldgen.structure.rule;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.gen.structure.Structure;
import org.haberno.terraloomed.platform.RegistryUtil;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;
import org.haberno.terraloomed.worldgen.terrain.Terrain;

public class StructureRules {

	public static void bootstrap() {
		register("cell_test", CellTest.CODEC);
	}
	
	public static CellTest cellTest(TagKey<Structure> targets, float cutoff, Terrain... terrainTypeBlacklist) {
		return new CellTest(cutoff, ImmutableSet.copyOf(terrainTypeBlacklist));
	}

	private static void register(String name, Codec<? extends StructureRule> value) {
		RegistryUtil.register(RTFBuiltInRegistries.STRUCTURE_RULE_TYPE, name, value);
	}
}
