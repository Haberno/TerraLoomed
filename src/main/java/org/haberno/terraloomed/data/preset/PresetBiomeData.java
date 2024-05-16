package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.data.preset.settings.MiscellaneousSettings;
import raccoonman.reterraforged.data.preset.settings.Preset;
import raccoonman.reterraforged.world.worldgen.biome.RTFBiomes;

public final class PresetBiomeData {
    public static final RegistryKey<Biome> BRYCE = createKey("bryce");
    public static final RegistryKey<Biome> COLD_STEPPE = createKey("cold_steppe");
    public static final RegistryKey<Biome> COLD_MARSHLAND = createKey("cold_marshland");
    public static final RegistryKey<Biome> FIR_FOREST = createKey("fir_forest");
    public static final RegistryKey<Biome> FLOWER_PLAINS = createKey("flower_plains");
    public static final RegistryKey<Biome> FROZEN_LAKE = createKey("frozen_lake");
    public static final RegistryKey<Biome> FROZEN_MARSH = createKey("frozen_marsh");
    public static final RegistryKey<Biome> LAKE = createKey("lake");
    public static final RegistryKey<Biome> MARSHLAND = createKey("marshland");
    public static final RegistryKey<Biome> SAVANNA_SCRUB = createKey("savanna_scrub");
    public static final RegistryKey<Biome> SHATTERED_SAVANNA_SCRUB = createKey("shattered_savanna_scrub");
    public static final RegistryKey<Biome> SNOWY_FIR_FOREST = createKey("snowy_fir_forest");
    public static final RegistryKey<Biome> SNOWY_TAIGA_SCRUB = createKey("snowy_taiga_scrub");
    public static final RegistryKey<Biome> STEPPE = createKey("steppe");
    public static final RegistryKey<Biome> STONE_FOREST = createKey("stone_forest");
    public static final RegistryKey<Biome> TAIGA_SCRUB = createKey("taiga_scrub");
    public static final RegistryKey<Biome> WARM_BEACH = createKey("warm_beach");
	
	public static void bootstrap(Preset preset, Registerable<Biome> ctx) {
		MiscellaneousSettings miscellaneousSettings = preset.miscellaneous();
		
		if(miscellaneousSettings.customBiomeFeatures) {
			RegistryEntryLookup<PlacedFeature> placedFeatures = ctx.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
			RegistryEntryLookup<ConfiguredCarver<?>> configuredWorldCarvers = ctx.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

		}
	}
	
    private static RegistryKey<Biome> createKey(String string) {
        return RegistryKey.of(RegistryKeys.BIOME, RTFCommon.location(string));
    }
}
