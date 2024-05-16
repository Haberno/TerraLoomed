package org.haberno.terraloomed.client.gui.screen.presetconfig;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.apache.commons.io.file.PathUtils;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.data.DataGenerator;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.world.gen.GeneratorOptions;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.client.gui.screen.page.LinkedPageScreen;
import raccoonman.reterraforged.client.gui.screen.presetconfig.PresetListPage.PresetEntry;
import raccoonman.reterraforged.data.RTFDataGen;
import raccoonman.reterraforged.data.preset.settings.Preset;

//FIXME pressing the create world screen before the pack is copied will fuck the game up (surprisingly noone seems to have run into this?)
public class PresetConfigScreen extends LinkedPageScreen {
	private CreateWorldScreen parent;
	
	public PresetConfigScreen(CreateWorldScreen parent) {
		this.parent = parent;
		this.currentPage = new PresetListPage(this);
	}
	
	@Override
	public void onClose() {
		super.onClose();

		this.minecraft.setScreen(this.parent);
	}
	
	public void setSeed(long seed) {
		//TODO update the seed edit box
		this.parent.getWorldCreator().setGeneratorOptionsHolder(this.getSettings().apply((options) -> {
			return new GeneratorOptions(seed, options.shouldGenerateStructures(), options.hasBonusChest());
		}));
	}
	
	public GeneratorOptionsHolder getSettings() {
		return this.parent.getWorldCreator().getGeneratorOptionsHolder();
	}

	public void applyPreset(PresetEntry preset) throws IOException {		
		Pair<Path, ResourcePackManager> path = this.parent.getScannedPack(this.parent.getWorldCreator().getGeneratorOptionsHolder().dataConfiguration());
		Path exportPath = path.getFirst().resolve("reterraforged-preset.zip");
		this.exportAsDatapack(exportPath, preset);
		ResourcePackManager repository = path.getSecond();
		repository.scanPacks();
		if(repository.enable("file/" + exportPath.getFileName())) {
			this.parent.applyDataPacks(repository, false, (data) -> {
			});
		}
	}
	
	public void exportAsDatapack(Path outputPath, PresetEntry presetEntry) throws IOException {
		Path datagenPath = Files.createTempDirectory("datagen-target-");
		Path datagenOutputPath = datagenPath.resolve("output");
		
		DynamicRegistryManager registryAccess = this.getSettings().getCombinedRegistryManager();

		Preset preset = presetEntry.getPreset();
		
		DataGenerator dataGenerator = RTFDataGen.makePreset(preset, registryAccess, datagenPath, datagenOutputPath);
		dataGenerator.run();
		copyToZip(datagenOutputPath, outputPath);
		PathUtils.deleteDirectory(datagenPath);
		
		RTFCommon.LOGGER.info("Exported datapack to {}", outputPath);
	}
	
	private static void copyToZip(Path input, Path output) {
		Map<String, String> env = ImmutableMap.of("create", "true");
	    URI uri = URI.create("jar:" + output.toUri());
	    try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
	        PathUtils.copyDirectory(input, fs.getPath("/"), StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
