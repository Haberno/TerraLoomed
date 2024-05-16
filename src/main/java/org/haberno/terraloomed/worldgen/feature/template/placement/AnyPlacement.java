package org.haberno.terraloomed.worldgen.feature.template.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.haberno.terraloomed.worldgen.feature.template.BlockUtils;
import org.haberno.terraloomed.worldgen.feature.template.template.Dimensions;
import org.haberno.terraloomed.worldgen.feature.template.template.NoopTemplateContext;

record AnyPlacement() implements TemplatePlacement<NoopTemplateContext> {
	public static final Codec<AnyPlacement> CODEC = Codec.unit(AnyPlacement::new);

	@Override
	public boolean canPlaceAt(WorldAccess world, BlockPos pos, Dimensions dimensions) {
		return true;
	}

	@Override
	public boolean canReplaceAt(WorldAccess world, BlockPos pos) {
		return !BlockUtils.isSolid(world, pos);
	}

	@Override
	public NoopTemplateContext createContext() {
		return new NoopTemplateContext();
	}

	@Override
	public Codec<AnyPlacement> codec() {
		return CODEC;
	}
}
