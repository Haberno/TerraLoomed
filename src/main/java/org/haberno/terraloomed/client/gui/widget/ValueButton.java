package org.haberno.terraloomed.client.gui.widget;

import java.util.function.Supplier;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ValueButton<T> extends ButtonWidget {
	private Text name;
	private T value;
	
	public ValueButton(int i, int j, int k, int l, Text component, PressAction onPress, T initial) {
		super(i, j, k, l, component, onPress, Supplier::get);

		this.name = component;
		this.setValue(initial);
	}
	
	public ValueButton(int i, int j, int k, int l, Text component, PressAction onPress, NarrationSupplier createNarration, T initial) {
		super(i, j, k, l, component, onPress, createNarration);

		this.name = component;
		this.setValue(initial);
	}
	
	public void setValue(T value) {
		this.value = value;
		
		this.setMessage(ScreenTexts.composeGenericOptionText(this.name, Text.literal(this.value.toString())));
	}
	
	public T getValue() {
		return this.value;
	}
}
