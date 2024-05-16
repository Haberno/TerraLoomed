package org.haberno.terraloomed.data.preset.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.worldgen.surface.rule.LayeredSurfaceRule.Layer;

import java.util.concurrent.CompletableFuture;

public class PresetSurfaceLayerProvider extends TagProvider<Layer> {
	private Preset preset;
	
	public PresetSurfaceLayerProvider(Preset preset, DataOutput packOutput, CompletableFuture<WrapperLookup> completableFuture) {
		super(packOutput, RTFRegistries.SURFACE_LAYERS, completableFuture);
		
		this.preset = preset;
	}

	@Override
	protected void configure(WrapperLookup provider) {

	}
}