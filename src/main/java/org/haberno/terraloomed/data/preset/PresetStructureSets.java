package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.structure.StructureSet;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.data.preset.settings.StructureSettings;

public class PresetStructureSets {

	public static void bootstrap(Preset preset, Registerable<StructureSet> ctx) {
//		StructureSettings structureSettings = preset.structures();
//
//		HolderGetter<Structure> structures = ctx.lookup(Registries.STRUCTURE);
//		
//		structureSettings.strongholds.ifPresent((settings) -> {
//			ctx.register(BuiltinStructureSets.STRONGHOLDS, new StructureSet(settings.getFilteredStructures().stream().map((entry) -> {
//				return makeEntry(structures, entry);
//			}).toList(), settings.makePlacement()));
//		});
//		
//		structureSettings.structureSets.forEach((key, settings) -> {
//			ctx.register(key, new StructureSet(settings.getFilteredStructures().stream().map((entry) -> {
//				return makeEntry(structures, entry);
//			}).toList(), settings.makePlacement()));
//		});
	}
	
//	private static StructureSelectionEntry makeEntry(HolderGetter<Structure> structures, StructureSettings.StructureEntry entry) {
//		return new StructureSelectionEntry(entry.structure().map(structures::getOrThrow, Holder::direct), entry.weight());
//	}
}
