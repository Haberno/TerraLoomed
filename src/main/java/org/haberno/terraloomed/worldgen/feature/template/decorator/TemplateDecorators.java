package org.haberno.terraloomed.worldgen.feature.template.decorator;

import com.mojang.serialization.Codec;
import org.haberno.terraloomed.platform.RegistryUtil;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;

public class TemplateDecorators {

	public static void bootstrap() {
		register("tree", TreeDecorator.CODEC);
	}
	
	public static TreeDecorator tree(net.minecraft.world.gen.treedecorator.TreeDecorator decorator) {
		return tree(decorator, decorator);
	}
	
	public static TreeDecorator tree(net.minecraft.world.gen.treedecorator.TreeDecorator decorator, net.minecraft.world.gen.treedecorator.TreeDecorator modifiedDecorator) {
		return new TreeDecorator(decorator, modifiedDecorator);
	}

	private static void register(String name, Codec<? extends TemplateDecorator<?>> placement) {
		RegistryUtil.register(RTFBuiltInRegistries.TEMPLATE_DECORATOR_TYPE, name, placement);
	}
}
