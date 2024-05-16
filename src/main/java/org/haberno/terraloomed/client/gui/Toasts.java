package org.haberno.terraloomed.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.SystemToast.Type;
import net.minecraft.text.Text;
import org.haberno.terraloomed.client.data.RTFTranslationKeys;

public final class Toasts {

	public static void notify(String message, Text description, Type id) {
		MinecraftClient mc = MinecraftClient.getInstance();
		SystemToast.add(mc.getToastManager(), id, Text.translatable(message), description);
	}
	
	public static void tryOrToast(String errorMessage, ThrowingRunnable r) {
		try {
			r.run();
		} catch(Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			Text messageComponent;
			if(message != null) {
				messageComponent = Text.literal(message);
			} else {
				messageComponent = Text.translatable(RTFTranslationKeys.NO_ERROR_MESSAGE);
			}
			
			notify(errorMessage, messageComponent, SystemToast.Type.PACK_LOAD_FAILURE);
		}
	}
	
	public interface ThrowingRunnable {
		void run() throws Exception;
	}
}
