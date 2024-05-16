package org.haberno.terraloomed.data.preset;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import org.haberno.terraloomed.worldgen.feature.template.decorator.TemplateDecorator;
import org.haberno.terraloomed.worldgen.feature.template.decorator.TemplateDecorators;
import org.haberno.terraloomed.worldgen.feature.template.decorator.TreeContext;

import java.util.List;

public class PresetTemplateDecoratorLists {
	public static final List<TemplateDecorator<TreeContext>> BEEHIVE_RARITY_0075 = ImmutableList.of(TemplateDecorators.tree(new BeehiveTreeDecorator(0.075F)));
	public static final List<TemplateDecorator<TreeContext>> BEEHIVE_RARITY_005 = ImmutableList.of(TemplateDecorators.tree(new BeehiveTreeDecorator(0.05F)));
	public static final List<TemplateDecorator<TreeContext>> BEEHIVE_RARITY_005_AND_0002 = ImmutableList.of(TemplateDecorators.tree(new BeehiveTreeDecorator(0.05F), new BeehiveTreeDecorator(0.002F)));
	public static final List<TemplateDecorator<TreeContext>> BEEHIVE_RARITY_002_AND_005 = ImmutableList.of(TemplateDecorators.tree(new BeehiveTreeDecorator(0.02F), new BeehiveTreeDecorator(0.05F)));
	public static final List<TemplateDecorator<TreeContext>> BEEHIVE_RARITY_0002_AND_005 = ImmutableList.of(TemplateDecorators.tree(new BeehiveTreeDecorator(0.002F), new BeehiveTreeDecorator(0.05F)));
}
