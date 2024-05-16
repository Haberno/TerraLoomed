package org.haberno.terraloomed.client.gui;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

public final class Tooltips {
	
	public static String translationKey(String key) {
		return key + ".tooltip";
	}
	
	public static String failTranslationKey(String key) {
		return translationKey(key) + ".fail";
	}
	
	public static Tooltip create(String key) {
		return Tooltip.of(Text.translatable(key));
	}
}
