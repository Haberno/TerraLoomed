package org.haberno.terraloomed.mixin;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//a bit unnecessary but whatever
//addRenderableWidget can be overridden so we don't use an access widener
@Deprecated
@Mixin(Screen.class)
public interface ScreenInvoker {

	@Invoker("addDrawableChild")
    <T extends Element & Drawable> T invokeAddRenderableWidget(T guiEventListener);
}
