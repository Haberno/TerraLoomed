package org.haberno.terraloomed.data;

import net.minecraft.SharedConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataGenerator.Pack;
import net.minecraft.data.DataOutput;
import net.minecraft.data.MetadataProvider;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.haberno.terraloomed.client.data.RTFLanguageProvider;
import org.haberno.terraloomed.client.data.RTFTranslationKeys;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.data.preset.tags.PresetBiomeTagsProvider;
import org.haberno.terraloomed.data.preset.tags.PresetBlockTagsProvider;
import org.haberno.terraloomed.data.preset.tags.PresetSurfaceLayerProvider;
import org.haberno.terraloomed.platform.DataGenUtil;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class RTFDataGen {
	public static final String DATAPACK_PATH = "data/reterraforged/datapacks";
	
	public static void generateResourcePacks(ResourcePackFactory resourcePackFactory) {
		Pack pack = resourcePackFactory.createPack();

		pack.addProvider(RTFLanguageProvider.EnglishUS::new);
		pack.addProvider((DataOutput output) -> MetadataProvider.create(output, Text.translatable(RTFTranslationKeys.METADATA_DESCRIPTION)));
	}
	
	@Deprecated
	public static DataGenerator makePreset(Preset preset, DynamicRegistryManager registryAccess, Path dataGenPath, Path dataGenOutputPath) {
		DataGenerator dataGenerator = new DataGenerator(dataGenPath, SharedConstants.getGameVersion(), true);
		Pack packGenerator = dataGenerator.new Pack(true, "preset", new DataOutput(dataGenOutputPath));
		CompletableFuture<RegistryWrapper.WrapperLookup> lookup = CompletableFuture.supplyAsync(() -> preset.buildPatch(registryAccess));
		
		packGenerator.addProvider((output) -> {
			return DataGenUtil.createRegistryProvider(output, lookup);
		});
		packGenerator.addProvider((output) -> {
			return new PresetBlockTagsProvider(output, lookup);
		});
		packGenerator.addProvider((output) -> {
			return new PresetSurfaceLayerProvider(preset, output, lookup);
		});
		packGenerator.addProvider((output) -> {
			return new PresetBiomeTagsProvider(preset, output, CompletableFuture.completedFuture(registryAccess));
		});
		packGenerator.addProvider((output) -> {
			return MetadataProvider.create(output, Text.translatable(RTFTranslationKeys.PRESET_METADATA_DESCRIPTION));
		});
		return dataGenerator;
	}
	
	public interface ResourcePackFactory {
		Pack createPack();
	}
	
	public interface DataPackFactory {
		Pack createPack(Identifier id);
	}
}
