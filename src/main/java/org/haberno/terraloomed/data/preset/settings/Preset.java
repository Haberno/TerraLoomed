package org.haberno.terraloomed.data.preset.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.*;
import org.haberno.terraloomed.data.preset.PresetNoiseGeneratorSettings;

import java.util.stream.Stream;

//TODO make this actually immutable when we rework the gui
public record Preset(WorldSettings world, SurfaceSettings surface, CaveSettings caves, ClimateSettings climate, TerrainSettings terrain, RiverSettings rivers, FilterSettings filters, MiscellaneousSettings miscellaneous) {
	public static final Codec<Preset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		WorldSettings.CODEC.fieldOf("world").forGetter(Preset::world),
		SurfaceSettings.CODEC.optionalFieldOf("surface", new SurfaceSettings(new SurfaceSettings.Erosion(30, 140, 40, 95, 95, 0.65F, 0.475F, 0.4F, 0.45F, 6.0F, 3.0F))).forGetter(Preset::surface),
		CaveSettings.CODEC.optionalFieldOf("caves", new CaveSettings()).forGetter(Preset::caves),
		ClimateSettings.CODEC.fieldOf("climate").forGetter(Preset::climate),
		TerrainSettings.CODEC.fieldOf("terrain").forGetter(Preset::terrain),
		RiverSettings.CODEC.fieldOf("rivers").forGetter(Preset::rivers),
		FilterSettings.CODEC.fieldOf("filters").forGetter(Preset::filters),
		MiscellaneousSettings.CODEC.fieldOf("miscellaneous").forGetter(Preset::miscellaneous)
	).apply(instance, Preset::new));
	
	public Preset copy() {
		return new Preset(this.world.copy(), this.surface.copy(), this.caves.copy(), this.climate.copy(), this.terrain.copy(), this.rivers.copy(), this.filters.copy(), /* this.structures.copy(), */this.miscellaneous.copy());
	}

	public RegistryWrapper.WrapperLookup buildPatch(DynamicRegistryManager registries) {
		RegistryBuilder builder = new RegistryBuilder();
//		this.addPatch(builder, RTFRegistries.PRESET, PresetData::bootstrap);
//		this.addPatch(builder, RTFRegistries.NOISE, PresetNoiseData::bootstrap);
//		this.addPatch(builder, RTFRegistries.BIOME_MODIFIER, PresetBiomeModifierData::bootstrap);
//		this.addPatch(builder, RTFRegistries.STRUCTURE_RULE, PresetStructureRuleData::bootstrap);
//		this.addPatch(builder, RTFRegistries.SURFACE_LAYERS, PresetSurfaceLayerData::bootstrap);
//		this.addPatch(builder, Registries.CONFIGURED_FEATURE, (preset, ctx) -> {
//			PresetConfiguredFeatures.bootstrap(preset, ctx);
//		});
//		this.addPatch(builder, Registries.CONFIGURED_CARVER, (preset, ctx) -> {
//			PresetConfiguredCarvers.bootstrap(preset, ctx);	
//		});
//		this.addPatch(builder, Registries.STRUCTURE_SET, PresetStructureSets::bootstrap);
//		this.addPatch(builder, Registries.PLACED_FEATURE, PresetPlacedFeatures::bootstrap);
//		this.addPatch(builder, Registries.BIOME, PresetBiomeData::bootstrap);
//		this.addPatch(builder, Registries.DIMENSION_TYPE, PresetDimensionTypes::bootstrap);
//		this.addPatch(builder, Registries.NOISE, PresetNoiseParameters::bootstrap);
//		this.addPatch(builder, Registries.DENSITY_FUNCTION, (preset, ctx) -> {
//			PresetNoiseRouterData.bootstrap(preset, ctx);
//			TBNoiseRouterData.bootstrap(ctx);
//		});
		this.addPatch(builder, RegistryKeys.CHUNK_GENERATOR_SETTINGS, PresetNoiseGeneratorSettings::bootstrap);
		Stream<RegistryWrapper.Impl<?>> stream = registries.streamAllRegistries().map((entry) -> {
			return entry.value().getReadOnlyWrapper();
		});
		return builder.createWrapperLookup(DynamicRegistryManager.of(Registries.REGISTRIES), stream );
	}
	
	private <T> void addPatch(RegistryBuilder builder, RegistryKey<? extends Registry<T>> key, Patch<T> patch) {
    	builder.addRegistry(key, (ctx) -> {
    		patch.apply(this, ctx);
    	});
    }
    
	private interface Patch<T> {
        void apply(Preset preset, Registerable<T> ctx);
	}
}
