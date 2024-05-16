package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.StructureTags;
import org.haberno.terraloomed.RTFCommon;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.worldgen.structure.rule.StructureRule;
import org.haberno.terraloomed.worldgen.structure.rule.StructureRules;
import org.haberno.terraloomed.worldgen.terrain.TerrainType;

public class PresetStructureRuleData {
	public static final RegistryKey<StructureRule> CELL_TEST = createKey("cell_test");
	
	public static void bootstrap(Preset preset, Registerable<StructureRule> ctx) {
		ctx.register(CELL_TEST, StructureRules.cellTest(StructureTags.VILLAGE, 0.225F, TerrainType.MOUNTAIN_CHAIN, TerrainType.MOUNTAINS_1, TerrainType.MOUNTAINS_2, TerrainType.MOUNTAINS_3));
	}
	
	private static RegistryKey<StructureRule> createKey(String name) {
        return RegistryKey.of(RTFRegistries.STRUCTURE_RULE, RTFCommon.location(name));
	}
}
