package org.haberno.terraloomed.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.haberno.terraloomed.client.gui.screen.presetconfig.PresetEditorPage;
import org.haberno.terraloomed.client.gui.widget.WidgetList.Entry;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WidgetList<T extends ClickableWidget> extends ElementListWidget<Entry<T>> {
	private boolean renderSelected;
	
    public WidgetList(MinecraftClient minecraft, int i, int j, int k, int l, int slotHeight) {
        super(minecraft, i, j, k, l);
    }
    
    public void select(T widget) {
    	for(Entry<T> entry : this.children()) {
    		if(entry.widget.equals(widget)) {
    			this.setSelected(entry);
    			return;
    		}
    	}
    }
    
    public <W extends T> W addWidget(W widget) {
        super.addEntry(new Entry<>(widget));
        return widget;
    }

    public void setRenderSelected(boolean renderSelected) {
    	this.renderSelected = renderSelected;
    }
    
    @Override
    protected boolean isSelectedEntry(int i) {
        return this.renderSelected && Objects.equals(this.getSelectedOrNull(), this.children().get(i));
    }

    @Override
    public int getRowWidth() {
        return this.width - 20;
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.getRowRight();
    }

    public static class Entry<T extends ClickableWidget> extends ElementListWidget.Entry<Entry<T>> {
        private T widget;

        public Entry(T widget) {
            this.widget = widget;
        }

        public T getWidget() {
        	return this.widget;
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(this.widget);
        }

        @Override
        public void render(DrawContext guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            int optionWidth = Math.min(396, width);
            int padding = (width - optionWidth) / 2;
            widget.setX(left + padding);
            widget.setY(top);
            widget.visible = true;
            widget.setWidth(optionWidth);
            widget.setHeight(height - 1);	
            if(widget instanceof PresetEditorPage.Preview preview) {
            	widget.setHeight(widget.getWidth());
            }
            widget.render(guiGraphics, mouseX, mouseY, partialTicks);
        }

		@Override
		public List<T> selectableChildren() {
			return Collections.singletonList(this.widget);
		}
    }
}
