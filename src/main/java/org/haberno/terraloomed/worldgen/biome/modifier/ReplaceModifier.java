package org.haberno.terraloomed.worldgen.biome.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

record ReplaceModifier(GenerationStep.Feature step, Optional<RegistryEntryList<Biome>> biomes, Map<RegistryKey<PlacedFeature>, RegistryEntry<PlacedFeature>> replacements) implements FabricBiomeModifier {
    public static final Codec<ReplaceModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GenerationStep.Feature.CODEC.fieldOf("step").forGetter(ReplaceModifier::step),
            Biome.REGISTRY_ENTRY_LIST_CODEC.optionalFieldOf("biomes").forGetter(ReplaceModifier::biomes),
            Codec.unboundedMap(RegistryKey.createCodec(RegistryKeys.PLACED_FEATURE), PlacedFeature.REGISTRY_CODEC).fieldOf("replacements").forGetter(ReplaceModifier::replacements)
    ).apply(instance, ReplaceModifier::new));
    @Override
    public void apply(BiomeSelectionContext selectionContext, BiomeModificationContext modificationContext) {
        if(this.biomes.isPresent() && !this.biomes.get().contains(selectionContext.getBiomeRegistryEntry())) {
            return;
        }

        GenerationSettings generationSettings = selectionContext.getBiome().getGenerationSettings();
        List<RegistryEntryList<PlacedFeature>> featureSteps = generationSettings.getFeatures();
        int index = this.step.ordinal();

        while (index >= featureSteps.size()) {
            featureSteps.add(RegistryEntryList.of()); //Double check
        }

        featureSteps.set(index, this.replace(featureSteps.get(index)));

        this.rebuildFlowerFeatures(generationSettings);
    }

    private RegistryEntryList<PlacedFeature> replace(RegistryEntryList<PlacedFeature> features) {
        List<RegistryEntry<PlacedFeature>> newList = new ArrayList<>(features.stream().toList());
        newList.replaceAll((f) -> {
            return f.getKey().map(this.replacements::get).orElse(f);
        });
        return RegistryEntryList.of(newList); //DoubleCheck
    }

    @Override
    public Codec<ReplaceModifier> codec() {
        return CODEC;
    }
}
