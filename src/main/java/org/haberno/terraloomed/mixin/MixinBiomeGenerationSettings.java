package org.haberno.terraloomed.mixin;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//TODO do this with access wideners instead
@Deprecated
@Mixin(GenerationSettings.class)
public interface MixinBiomeGenerationSettings {
	@Accessor
	List<RegistryEntryList<PlacedFeature>> getFeatures();
	
	@Accessor
	void setFlowerFeatures(Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures);
}
