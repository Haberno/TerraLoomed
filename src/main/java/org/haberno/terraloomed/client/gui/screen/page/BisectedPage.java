package org.haberno.terraloomed.client.gui.screen.page;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.haberno.terraloomed.client.gui.ColumnAlignment;
import org.haberno.terraloomed.client.gui.screen.page.LinkedPageScreen.Page;
import org.haberno.terraloomed.client.gui.widget.WidgetList;

public abstract class BisectedPage<S extends Screen, L extends ClickableWidget, R extends ClickableWidget> implements Page {
	protected S screen;
	protected WidgetList<L> left;
	protected WidgetList<R> right;
	
	public BisectedPage(S screen) {
		this.screen = screen;
	}
	
	@Override
	public void init() {
		ColumnAlignment alignment = new ColumnAlignment(this.screen, 4, 0, 10, 30);
		this.left = alignment.addColumn(0.7F, this::createAndPositionColumn);
		this.right = alignment.addColumn(0.3F, this::createAndPositionColumn);
	}
	
	private <T extends ClickableWidget> WidgetList<T> createAndPositionColumn(int left, int top, int columnWidth, int height, int horizontalPadding, int verticalPadding) {
		final int padding = 30;
		final int slotHeight = 25;
		WidgetList<T> list = new WidgetList<>(this.screen.client, columnWidth, height, padding, height - padding, slotHeight);
		list.setX(left); //Haberno todo
		return list;
	}
}