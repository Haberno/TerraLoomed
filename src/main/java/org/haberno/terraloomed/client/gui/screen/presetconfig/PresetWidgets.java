package org.haberno.terraloomed.client.gui.screen.presetconfig;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.haberno.terraloomed.client.data.RTFTranslationKeys;
import org.haberno.terraloomed.client.gui.Toasts;
import org.haberno.terraloomed.client.gui.Tooltips;
import org.haberno.terraloomed.client.gui.widget.Label;
import org.haberno.terraloomed.client.gui.widget.Slider;
import org.haberno.terraloomed.client.gui.widget.Slider.Format;
import org.haberno.terraloomed.client.gui.widget.ValueButton;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;

final class PresetWidgets {
	
	public static TextFieldWidget createEditBox(TextRenderer font, Consumer<String> responder, Text prompt) {
		TextFieldWidget box = new TextFieldWidget(font, -1, -1, -1, -1, ScreenTexts.EMPTY);
		box.setChangedListener(responder);
		box.setPlaceholder(prompt);
		return box;
	}
	
	public static Label createLabel(String text) {
		return createLabel(Text.translatable(text));
	}
	
	public static Label createLabel(Text text) {
		return new Label(-1, -1, -1, -1, text);
	}

	private static Slider createSlider(float initial, float min, float max, String text, Format format, Slider.Callback callback) {
		Slider slider = new Slider(-1, -1, -1, -1, initial, min, max, Text.translatable(text), format, callback);
		slider.setTooltip(Tooltips.create(Tooltips.translationKey(text)));
		return slider;
	}

	public static Slider createFloatSlider(float initial, float min, float max, String text, Slider.Callback callback) {
		return createSlider(initial, min, max, text, Format.FLOAT, callback);
	}
	
	public static Slider createIntSlider(int initial, int min, int max, String text, Slider.Callback callback) {
		return createSlider(initial, min, max, text, Format.INT, callback);
	}
	
	public static <T extends Enum<T>> CyclingButtonWidget<T> createCycle(T[] values, T initial, String text, CyclingButtonWidget.UpdateCallback<T> callback) {
		return createCycle(ImmutableList.copyOf(values), initial, text, callback, T::name);
	}

	public static <T> CyclingButtonWidget<T> createCycle(Collection<T> values, T initial, String text, CyclingButtonWidget.UpdateCallback<T> callback, Function<T, String> name) {
		return createCycle(values, initial, Optional.of(text), callback, name);
	}
	
	public static <T> CyclingButtonWidget<T> createCycle(Collection<T> values, T initial, Optional<String> text, CyclingButtonWidget.UpdateCallback<T> callback, Function<T, String> name) {
		CyclingButtonWidget.Builder<T> builder = CyclingButtonWidget.<T>builder((e) -> {
			return Text.literal(name.apply(e));
		}).initially(initial).values(values);
		if(text.isEmpty()) {
			builder = builder.omitKeyText();
		}
		CyclingButtonWidget<T> button = builder.build(-1, -1, -1, -1, text.map(Text::translatable).orElse(null), callback);
		text.ifPresent((key) -> {
			button.setTooltip(Tooltips.create(Tooltips.translationKey(key)));
		});
		return button;
	}
	
	public static CyclingButtonWidget<Boolean> createToggle(boolean initial, String text, CyclingButtonWidget.UpdateCallback<Boolean> callback) {
		CyclingButtonWidget<Boolean> button = CyclingButtonWidget.onOffBuilder(Text.translatable(RTFTranslationKeys.GUI_BUTTON_TRUE), Text.translatable(RTFTranslationKeys.GUI_BUTTON_FALSE)).initially(initial).build(-1, -1, -1, -1, Text.translatable(text), callback);
		button.setTooltip(Tooltips.create(Tooltips.translationKey(text)));
		return button;
	}
	
	public static ButtonWidget createThrowingButton(String text, Toasts.ThrowingRunnable run) {
		return ButtonWidget.builder(Text.translatable(text), (b) -> {
			Toasts.tryOrToast(Tooltips.failTranslationKey(text), run);
		}).build();
	}
	
	public static <T> ValueButton<T> createValueButton(String text, ButtonWidget.PressAction onPress, T initial) {
		ValueButton<T> button = new ValueButton<>(-1, -1, -1, -1, Text.translatable(text), onPress, initial);
		button.setTooltip(Tooltips.create(Tooltips.translationKey(text)));
		return button;
	}
	
	@SuppressWarnings("unchecked")
	public static ValueButton<Integer> createRandomButton(String text, int initial, Consumer<Integer> onPress) {
		return createValueButton(text, (button) -> {
			if(button instanceof ValueButton valueButton) {
				valueButton.setValue(ThreadLocalRandom.current().nextInt());
				onPress.accept((Integer) valueButton.getValue());
			}
		}, initial);
	}
}
