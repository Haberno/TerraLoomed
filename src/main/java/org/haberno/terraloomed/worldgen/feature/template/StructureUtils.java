package org.haberno.terraloomed.worldgen.feature.template;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;

public class StructureUtils {

    public static boolean hasOvergroundStructure(RegistryWrapper<Structure> structures, Chunk chunk) {
    	Map<Structure, LongSet> references = chunk.getStructureReferences();
    	
    	for (Structure structure : structures.streamEntries().map(RegistryEntry::value).filter((structure) -> structure.getFeatureGenerationStep() == GenerationStep.Feature.SURFACE_STRUCTURES).toList()) {
            LongSet refs = references.get(structure);
            if (refs != null && refs.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasStructure(Chunk chunk, Structure structure) {
        LongSet refs = chunk.getStructureReferences().get(structure);
        return refs != null && refs.size() > 0;
    }
}
