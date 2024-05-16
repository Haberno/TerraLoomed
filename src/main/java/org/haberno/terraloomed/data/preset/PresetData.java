package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.registries.RTFRegistries;

//TODO support different presets per dimension
public class PresetData {
	public static final RegistryKey<Preset> PRESET = RTFRegistries.createKey(RTFRegistries.PRESET, "preset");
	
	public static void bootstrap(Preset preset, Registerable<Preset> ctx) {
		ctx.register(PRESET, preset);
	}
}
