package org.haberno.terraloomed.data.preset.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import raccoonman.reterraforged.data.preset.PresetSurfaceLayerData;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.tags.RTFSurfaceLayerTags;
import raccoonman.reterraforged.world.worldgen.surface.rule.LayeredSurfaceRule;

public class PresetSurfaceLayerProvider extends TagProvider<LayeredSurfaceRule.Layer> {
	private Preset preset;
	
	public PresetSurfaceLayerProvider(Preset preset, DataOutput packOutput, CompletableFuture<WrapperLookup> completableFuture) {
		super(packOutput, RTFRegistries.SURFACE_LAYERS, completableFuture);
		
		this.preset = preset;
	}

	@Override
	protected void configure(WrapperLookup provider) {

	}
}