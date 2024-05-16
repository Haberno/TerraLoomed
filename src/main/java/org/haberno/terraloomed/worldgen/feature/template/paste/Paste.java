package org.haberno.terraloomed.worldgen.feature.template.paste;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.haberno.terraloomed.worldgen.feature.template.placement.TemplatePlacement;
import org.haberno.terraloomed.worldgen.feature.template.template.TemplateContext;

public interface Paste {
    <T extends TemplateContext>	boolean apply(WorldAccess world, T ctx, BlockPos origin, BlockMirror mirror, BlockRotation rotation, TemplatePlacement<T> placement, PasteConfig config);
}
