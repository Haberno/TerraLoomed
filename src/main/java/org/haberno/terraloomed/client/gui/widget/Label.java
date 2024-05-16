package org.haberno.terraloomed.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class Label extends ButtonWidget {
    
	public Label(int x, int y, int width, int height, Text component) {
    	this(x, y, width, height, (b) -> {}, component);
	}
	
	public Label(int x, int y, int width, int height, PressAction onPress, Text component) {
    	super(x, y, width, height, component, onPress, Supplier::get);
	}

	@Override
    public void playDownSound(SoundManager soundManager) {
    }

	@Override
	public void renderWidget(DrawContext graphics, int mouseX, int mouseY, float partialTicks) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
        graphics.drawTextWithShadow(minecraft.textRenderer, this.getMessage(), this.getX(), this.getY() + (this.height - 8) / 2, 0xFFFFFF);
	}
}
