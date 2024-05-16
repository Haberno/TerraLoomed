package org.haberno.terraloomed.worldgen.biome.modifier;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.haberno.terraloomed.mixin.MixinBiomeGenerationSettings;

public interface FabricBiomeModifier extends BiomeModifier {
    void apply(BiomeSelectionContext selectionContext, BiomeModificationContext modificationContext);

    default void rebuildFlowerFeatures( GenerationSettings generationSettings) {
        if(generationSettings instanceof MixinBiomeGenerationSettings biomeGenerationSettings) {
            biomeGenerationSettings.setFlowerFeatures(Suppliers.memoize(() -> {
                return biomeGenerationSettings.getFeatures().stream().flatMap(RegistryEntryList::stream).map(RegistryEntry::value).flatMap(PlacedFeature::getDecoratedFeatures).filter((configuredFeature) -> {
                    return configuredFeature.feature() == Feature.FLOWER;
                }).collect(ImmutableList.toImmutableList());
            }));
        }
    }
}
