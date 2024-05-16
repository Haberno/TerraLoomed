package org.haberno.terraloomed.worldgen.biome.modifier;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

record AddModifier(Order order, GenerationStep.Feature step, Optional<Filter> biomes, RegistryEntryList<PlacedFeature> features) implements FabricBiomeModifier {
    public static final Codec<AddModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Order.CODEC.fieldOf("order").forGetter(AddModifier::order),
            GenerationStep.Feature.CODEC.fieldOf("step").forGetter(AddModifier::step),
            Filter.CODEC.optionalFieldOf("biomes").forGetter(AddModifier::biomes),
            PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddModifier::features)
    ).apply(instance, AddModifier::new));

    @Override
    public Codec<AddModifier> codec() {
        return CODEC;
    }

    @Override
    public void apply(BiomeSelectionContext selectionContext, BiomeModificationContext modificationContext) {
        if(this.biomes.isPresent() && !this.biomes.get().test(selectionContext.getBiomeRegistryEntry())) {
            return;
        }

        GenerationSettings generationSettings = selectionContext.getBiome().getGenerationSettings();
        List<RegistryEntryList<PlacedFeature>> featureSteps = generationSettings.getFeatures();
        int index = this.step.ordinal();

        while (index >= featureSteps.size()) {
            featureSteps.add(RegistryEntryList.of());
        }

        featureSteps.set(index, this.add(featureSteps.get(index)));

        this.rebuildFlowerFeatures(generationSettings);
    }

    private RegistryEntryList<PlacedFeature> add(@Nullable RegistryEntryList<PlacedFeature> values) {
        if (values == null) return this.features;
        return RegistryEntryList.of(this.order.add(values.stream().toList(), this.features.stream().toList()));
    }
}
