package org.haberno.terraloomed.client.gui.screen.page;

import java.util.Optional;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import raccoonman.reterraforged.client.gui.widget.Label;

public abstract class LinkedPageScreen extends Screen {
	public ButtonWidget previousButton,
				  nextButton,
				  cancelButton,
				  doneButton;
	protected Page currentPage;
	
	protected LinkedPageScreen() {
		super(ScreenTexts.EMPTY);
	}
	
	public void setPage(Page page) {
		this.currentPage.onClose();
		this.currentPage = page;
		this.clearAndInit();
	}
	
	@Override
	public void init() {
		super.init();
		
		int buttonsCenter = this.width / 2;
        int buttonWidth = 50;
        int buttonHeight = 20;
        int buttonPad = 2;
        int buttonsRow = this.height - 25;
       
		this.previousButton = ButtonWidget.builder(Text.literal("<<"), (b) -> {
			this.currentPage.previous().ifPresent(this::setPage);
		}).dimensions(buttonsCenter - (buttonWidth * 2 + (buttonPad * 3)), buttonsRow, buttonWidth, buttonHeight).build();
		this.previousButton.active = this.currentPage.previous().isPresent();

		this.nextButton = ButtonWidget.builder(Text.literal(">>"), (b) -> {
			this.currentPage.next().ifPresent(this::setPage);
		}).dimensions(buttonsCenter + buttonWidth + (buttonPad * 3), buttonsRow, buttonWidth, buttonHeight).build();
		this.nextButton.active = this.currentPage.next().isPresent();
		
		this.cancelButton = ButtonWidget.builder(ScreenTexts.CANCEL, (b) -> {
			this.close();
		}).dimensions(buttonsCenter - buttonWidth - buttonPad, buttonsRow, buttonWidth, buttonHeight).build();

		this.doneButton = ButtonWidget.builder(ScreenTexts.DONE, (b) -> {
			this.onDone();
			this.close();
		}).dimensions(buttonsCenter + buttonPad, buttonsRow, buttonWidth, buttonHeight).build();
		
		this.currentPage.init();

		// these must be overlayed onto the current page
		this.addDrawable(new Label(16, 15, 20, 20, this.currentPage.title()));

		this.addDrawableChild(this.cancelButton);
		this.addDrawableChild(this.doneButton);
		this.addDrawableChild(this.previousButton);
		this.addDrawableChild(this.nextButton);
	}

	@Override
	public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.renderBackground(guiGraphics, mouseY, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void close() {
		this.currentPage.onClose();
	}
	
	public void onDone() {
		this.currentPage.onDone();
	}
	
	public interface Page {
		Text title();
		
		void init();
		
		Optional<Page> previous();
		
		Optional<Page> next();
		
		default void onClose() {
		}
		
		default void onDone() {
		}
	}
}
