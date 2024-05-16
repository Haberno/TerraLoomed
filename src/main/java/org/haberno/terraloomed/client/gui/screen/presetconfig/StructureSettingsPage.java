package org.haberno.terraloomed.client.gui.screen.presetconfig;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSet.WeightedEntry;
import net.minecraft.text.Text;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;
import net.minecraft.world.gen.structure.Structure;
import org.haberno.terraloomed.client.data.RTFTranslationKeys;
import org.haberno.terraloomed.client.gui.screen.page.LinkedPageScreen.Page;
import org.haberno.terraloomed.client.gui.screen.presetconfig.PresetListPage.PresetEntry;
import org.haberno.terraloomed.data.preset.settings.Preset;

import java.util.Optional;
import java.util.Set;

public class StructureSettingsPage extends PresetEditorPage {

	public StructureSettingsPage(PresetConfigScreen screen, PresetEntry preset) {
		super(screen, preset);
	}

	@Override
	public Text title() {
		return Text.translatable(RTFTranslationKeys.GUI_STRUCTURE_SETTINGS_TITLE);
	}
	
	@Override
	public void init() {
		super.init();
		
		Preset preset = this.preset.getPreset();
//		StructureSettings structures = preset.structures();
//		
//		WorldCreationContext worldSettings = this.screen.getSettings();
//		RegistryAccess.Frozen registries = worldSettings.worldgenLoadContext();
//		
//		registries.lookupOrThrow(Registries.STRUCTURE_SET).listElements().filter((holder) -> {
//			return isOverworldStructureSet(worldSettings.selectedDimensions(), holder);
//		}).forEach((holder) -> {
//			ResourceKey<StructureSet> key = holder.key();
//			StructureSet set = holder.value();
//			StructurePlacement placement = set.placement();
//			if(key.equals(BuiltinStructureSets.STRONGHOLDS) && placement instanceof ConcentricRingsStructurePlacement p) {
//				structures.strongholds = structures.strongholds.isPresent() 
//					? structures.strongholds 
//					: Optional.of(new StructureSettings.StrongholdSettings(set.structures().stream().map(StructureSettingsPage::makeEntry).toList(), p.locateOffset, p.frequencyReductionMethod, p.frequency, p.salt(), p.exclusionZone, false, p.distance(), p.spread(), p.count(), p.preferredBiomes()));
//			}
//			if(placement instanceof RandomSpreadStructurePlacement p) {
//				structures.structureSets.computeIfAbsent(key, (k) -> {
//					return new StructureSettings.StructureSetSettings(set.structures().stream().map(StructureSettingsPage::makeEntry).toList(), p.locateOffset, p.frequencyReductionMethod, p.frequency, p.salt(), p.exclusionZone, false, p.spacing(), p.separation(), p.spreadType());
//				});
//			}
//		});
//		
//		structures.strongholds.ifPresent((settings) -> {
//			
//			
//		});
//		
//		structures.structureSets.forEach((key, settings) -> {
//			class Holder {
//				public Slider slider;
//			}
//			Holder separationHolder = new Holder();
//			
//			Slider spacing = PresetWidgets.createIntSlider(settings.spacing, 0, 1000, RTFTranslationKeys.GUI_SLIDER_SPACING, (slider, value) -> {
//				value = Math.max(value, separationHolder.slider.getValue() + slider.getSliderValue(1.0F));
//				settings.spacing = (int) slider.scaleValue((float) value);
//				return value;
//			});
//			Slider separation = PresetWidgets.createIntSlider(settings.separation, 0, 1000, RTFTranslationKeys.GUI_SLIDER_SEPARATION, (slider, value) -> {
//				value = Math.min(value, spacing.getValue() - slider.getSliderValue(1.0F));
//				settings.separation = (int) slider.scaleValue((float) value);
//				return value;
//			});
//			ValueButton<Integer> salt = PresetWidgets.createRandomButton(RTFTranslationKeys.GUI_BUTTON_SALT, settings.salt, (value) -> {
//				settings.salt = value;
//			});
//			CycleButton<Boolean> disabled = PresetWidgets.createToggle(settings.disabled, RTFTranslationKeys.GUI_BUTTON_DISABLED, (button, value) -> {
//				settings.disabled = value;
//			});
//			
//			this.left.addWidget(PresetWidgets.createLabel(key.location().toString()));
//			this.left.addWidget(spacing);
//			this.left.addWidget(separation);
//			this.left.addWidget(salt);
//			this.left.addWidget(disabled);
//		});
	}

	@Override
	public Optional<Page> previous() {
		return Optional.of(new FilterSettingsPage(this.screen, this.preset));
	}

	@Override
	public Optional<Page> next() {
		return Optional.of(new MiscellaneousPage(this.screen, this.preset));
	}
	
//	private static StructureSettings.StructureEntry makeEntry(StructureSelectionEntry entry) {
//		return new StructureSettings.StructureEntry(entry.structure().unwrap(), entry.weight());
//	}

	private static boolean isOverworldStructureSet(DimensionOptionsRegistryHolder dimensions, RegistryEntry.Reference<StructureSet> holder) {
		Set<RegistryEntry<Biome>> overworldBiomes = dimensions.getChunkGenerator().getBiomeSource().getBiomes();
		for(WeightedEntry structureEntry : holder.value().structures()) {
			Structure structure = structureEntry.structure().value();

			for(RegistryEntry<Biome> biome : structure.getValidBiomes()) {
				if(overworldBiomes.contains(biome)) {
					return true;
				}
			}
		}
		return false;
	}
}
