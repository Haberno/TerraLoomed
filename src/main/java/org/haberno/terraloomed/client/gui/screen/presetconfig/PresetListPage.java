package org.haberno.terraloomed.client.gui.screen.presetconfig;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DataResult.PartialResult;
import com.mojang.serialization.JsonOps;
import io.netty.util.internal.StringUtil;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;
import org.apache.commons.compress.utils.FileNameUtils;
import org.haberno.terraloomed.RTFCommon;
import org.haberno.terraloomed.client.data.RTFTranslationKeys;
import org.haberno.terraloomed.client.gui.Toasts;
import org.haberno.terraloomed.client.gui.screen.page.BisectedPage;
import org.haberno.terraloomed.client.gui.screen.page.LinkedPageScreen.Page;
import org.haberno.terraloomed.client.gui.screen.presetconfig.PresetListPage.PresetEntry;
import org.haberno.terraloomed.client.gui.widget.Label;
import org.haberno.terraloomed.client.gui.widget.WidgetList;
import org.haberno.terraloomed.client.gui.widget.WidgetList.Entry;
import org.haberno.terraloomed.data.preset.settings.BuiltinPresets;
import org.haberno.terraloomed.data.preset.settings.Preset;
import org.haberno.terraloomed.platform.ConfigUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

class PresetListPage extends BisectedPage<PresetConfigScreen, PresetEntry, ClickableWidget> {
	private static final Path PRESET_PATH = ConfigUtil.rtf("presets");
	private static final Path EXPORT_PATH = ConfigUtil.rtf("exports");
	private static final Path LEGACY_PRESET_PATH = ConfigUtil.legacy("presets");
	
	private static final Predicate<String> IS_VALID = Pattern.compile("^[A-Za-z0-9\\-_ ]+$").asPredicate();

	private TextFieldWidget input;
	private ButtonWidget createPreset;
	private ButtonWidget deletePreset;
	private ButtonWidget exportAsDatapack;
	private ButtonWidget copyPreset;
	private ButtonWidget openPresetFolder;
	private ButtonWidget openExportFolder;
	
	public PresetListPage(PresetConfigScreen screen) {
		super(screen);
		
		try {
			if(!Files.exists(PRESET_PATH)) Files.createDirectory(PRESET_PATH);
			if(!Files.exists(EXPORT_PATH)) Files.createDirectory(EXPORT_PATH);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Text title() {
		return Text.translatable(RTFTranslationKeys.GUI_SELECT_PRESET_TITLE);
	}

	@Override
	public void init() {
		super.init();
		
		this.input = PresetWidgets.createEditBox(this.screen.client.textRenderer, (text) -> {
			boolean isValid = this.isValidPresetName(text);
			final int white = 14737632;
			final int red = 0xFFFF3F30;
			this.createPreset.active = isValid;
			this.input.setEditableColor(isValid ? white : red);
		}, Text.translatable(RTFTranslationKeys.GUI_INPUT_PROMPT).formatted(Formatting.DARK_GRAY));
		this.createPreset = PresetWidgets.createThrowingButton(RTFTranslationKeys.GUI_BUTTON_CREATE, () -> {
			new PresetEntry(Text.literal(this.input.getText()), BuiltinPresets.makeLegacyDefault(), false, this).save();
			this.rebuildPresets();
			this.input.setText(StringUtil.EMPTY_STRING);
		});
		this.createPreset.active = this.isValidPresetName(this.input.getText());
		this.copyPreset = PresetWidgets.createThrowingButton(RTFTranslationKeys.GUI_BUTTON_COPY, () -> {
			PresetEntry preset = this.left.getSelectedOrNull().getWidget();
			String name = preset.getName().getString();
			int counter = 1;
			String uniqueName;
			while(Files.exists(PRESET_PATH.resolve((uniqueName = name + " (" + counter + ")") + ".json"))) { 
				counter++;
			}
			new PresetEntry(Text.literal(uniqueName), preset.getPreset().copy(), false, this).save();
			this.rebuildPresets();
		});
		this.deletePreset = PresetWidgets.createThrowingButton(RTFTranslationKeys.GUI_BUTTON_DELETE, () -> {
			PresetEntry preset = this.left.getSelectedOrNull().getWidget();
			Files.delete(preset.getPath());
			this.rebuildPresets();
		});
		this.openPresetFolder = PresetWidgets.createThrowingButton(RTFTranslationKeys.GUI_BUTTON_OPEN_PRESET_FOLDER, () -> {
			Util.getOperatingSystem().open(PRESET_PATH.toUri());
			this.rebuildPresets();
		});
		this.openExportFolder = PresetWidgets.createThrowingButton(RTFTranslationKeys.GUI_BUTTON_OPEN_EXPORT_FOLDER, () -> {
			Util.getOperatingSystem().open(EXPORT_PATH.toUri());
			this.rebuildPresets();
		});
		this.exportAsDatapack = PresetWidgets.createThrowingButton(RTFTranslationKeys.GUI_BUTTON_EXPORT_AS_DATAPACK, () -> {
			PresetEntry preset = this.left.getSelectedOrNull().getWidget();
			Path path = EXPORT_PATH.resolve(preset.getName().getString() + ".zip");
			this.screen.exportAsDatapack(path, preset);
			this.rebuildPresets();
			
			Toasts.notify(RTFTranslationKeys.GUI_BUTTON_EXPORT_SUCCESS, Text.literal(path.toString()), SystemToast.Type.WORLD_BACKUP);
		});

		this.right.addWidget(this.input);
		this.right.addWidget(this.createPreset);
		this.right.addWidget(this.copyPreset);
		this.right.addWidget(this.deletePreset);
		this.right.addWidget(this.openPresetFolder);
		this.right.addWidget(this.openExportFolder);
		this.right.addWidget(this.exportAsDatapack);
		
		this.left.setRenderSelected(true);
		
		try {
			this.rebuildPresets();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Optional<Page> previous() {
		return Optional.empty();
	}

	@Override
	public Optional<Page> next() {
		return Optional.ofNullable(this.left).map(WidgetList::getSelectedOrNull).map((e) -> {
			PresetEntry entry = e.getWidget();
			if(entry.isBuiltin()) {
				entry = new PresetEntry(entry.name, entry.preset.copy(), true, (b) -> {});
			}
			return new WorldSettingsPage(this.screen, entry);
		});
	}
	
	@Override
	public void onDone() {
		super.onDone();
		
		Entry<PresetEntry> selected = this.left.getSelectedOrNull();
		if(selected != null) {
			try {
				this.screen.applyPreset(selected.getWidget());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void selectPreset(@Nullable PresetEntry entry) {
		boolean active = entry != null;
		
		this.screen.doneButton.active = active;
		this.copyPreset.active = active;
		this.deletePreset.active = active && !entry.isBuiltin();
		this.exportAsDatapack.active = active;
		this.screen.nextButton.active = active;
	}
	
	private void rebuildPresets() throws IOException {
		this.selectPreset(null);
		
		List<PresetEntry> entries = new ArrayList<>();
		entries.addAll(this.listPresets(PRESET_PATH));
		entries.addAll(this.listPresets(LEGACY_PRESET_PATH));

		entries.add(new PresetEntry(Text.translatable(RTFTranslationKeys.GUI_DEFAULT_PRESET_NAME).formatted(Formatting.GRAY), BuiltinPresets.makeDefault(), true, this));
		entries.add(new PresetEntry(Text.translatable(RTFTranslationKeys.GUI_DEFAULT_LEGACY_PRESET_NAME).formatted(Formatting.GRAY), BuiltinPresets.makeLegacyDefault(), true, this));
		entries.add(new PresetEntry(Text.translatable(RTFTranslationKeys.GUI_BEAUTIFUL_PRESET_NAME).formatted(Formatting.GRAY), BuiltinPresets.makeLegacyBeautiful(), true, this));
		entries.add(new PresetEntry(Text.translatable(RTFTranslationKeys.GUI_HUGE_BIOMES_PRESET_NAME).formatted(Formatting.GRAY), BuiltinPresets.makeLegacyHugeBiomes(), true, this));
		entries.add(new PresetEntry(Text.translatable(RTFTranslationKeys.GUI_LITE_PRESET_NAME).formatted(Formatting.GRAY), BuiltinPresets.makeLegacyLite(), true, this));
		entries.add(new PresetEntry(Text.translatable(RTFTranslationKeys.GUI_VANILLAISH_PRESET_NAME).formatted(Formatting.GRAY), BuiltinPresets.makeLegacyVanillaish(), true, this));
		this.left.replaceEntries(entries.stream().map(WidgetList.Entry::new).toList());
	}
	
	private boolean isValidPresetName(String text) {
		return IS_VALID.test(text) && !this.hasPresetWithName(text);
	}
	
	private boolean hasPresetWithName(String name) {
		return this.left.children().stream().filter((entry) -> {
			return entry.getWidget().getName().getString().equals(name);
		}).findAny().isPresent();
	}
	
	private List<PresetEntry> listPresets(Path path) throws IOException	{
		List<PresetEntry> presets = new ArrayList<>();
		if(Files.exists(path)) {
			for(Path presetPath : Files.list(path)
				.filter(Files::isRegularFile)
				.toList()
			) {
				try(Reader reader = Files.newBufferedReader(presetPath)) {
					String base = FileNameUtils.getBaseName(presetPath.toString());
					DataResult<Preset> result = Preset.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader));
					Optional<PartialResult<Preset>> error = result.error();
					if(error.isPresent()) {
						RTFCommon.LOGGER.error(error.get().message());
						continue;
					}
					Preset preset = result.result().get();
					presets.add(new PresetEntry(Text.literal(base), preset, false, this));
				}
			}
		}
		return presets;
	}
	
	public static class PresetEntry extends Label {
		private Text name;
		private Preset preset;
		private boolean builtin;
		
		public PresetEntry(Text name, Preset preset, boolean builtin, PressAction onPress) {
			super(-1, -1, -1, -1, onPress, name);
			
			this.name = name;
			this.preset = preset;
			this.builtin = builtin;
		}
		
		public PresetEntry(Text name, Preset preset, boolean builtin, PresetListPage page) {
			this(name, preset, builtin, (b) -> {
				if(b instanceof PresetEntry entry) {
					page.selectPreset(entry);
				}
			});
		}

		public Text getName() {
			return this.name;
		}
		
		public Preset getPreset() {
			return this.preset;
		}
		
		public boolean isBuiltin() {
			return this.builtin;
		}
		
		public Path getPath() {
			return PRESET_PATH.resolve(this.name.getString() + ".json");
		}
		
		//FIXME delete old pack before save
		public void save() throws IOException {
			if(!this.builtin) {
				try(
					Writer writer = Files.newBufferedWriter(this.getPath());
					JsonWriter jsonWriter = new JsonWriter(writer);
				) {
					JsonElement element = Preset.CODEC.encodeStart(JsonOps.INSTANCE, this.preset).result().orElseThrow();
					jsonWriter.setSerializeNulls(false);
					jsonWriter.setIndent("  ");
					JsonHelper.writeSorted(jsonWriter, element, null);
				}
			}
		}
	}
}
