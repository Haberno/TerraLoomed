package org.haberno.terraloomed.client.gui.screen.presetconfig;

import java.util.Optional;
import net.minecraft.text.Text;
import raccoonman.reterraforged.client.data.RTFTranslationKeys;
import raccoonman.reterraforged.client.gui.screen.page.LinkedPageScreen.Page;
import raccoonman.reterraforged.client.gui.screen.presetconfig.PresetListPage.PresetEntry;
import raccoonman.reterraforged.data.preset.settings.CaveSettings;
import raccoonman.reterraforged.data.preset.settings.Preset;

public class CaveSettingsPage extends PresetEditorPage {
	
	public CaveSettingsPage(PresetConfigScreen screen, PresetEntry preset) {
		super(screen, preset);
	}

	@Override
	public Text title() {
		return Text.translatable(RTFTranslationKeys.GUI_CAVE_SETTINGS_TITLE);
	}

	@Override
	public void init() {
		super.init();

		Preset preset = this.preset.getPreset();
		CaveSettings caves = preset.caves();


	}
	
	@Override
	public Optional<Page> previous() {
		return Optional.of(new SurfaceSettingsPage(this.screen, this.preset));
	}

	@Override
	public Optional<Page> next() {
		return Optional.of(new ClimateSettingsPage(this.screen, this.preset));
	}
}
