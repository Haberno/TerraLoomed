package org.haberno.terraloomed.worldgen.feature.placement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.placementmodifier.AbstractConditionalPlacementModifier;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.cell.Cell;
import org.jetbrains.annotations.Nullable;

abstract class CellFilter extends AbstractConditionalPlacementModifier {
	
	@Override
	protected boolean shouldPlace(FeaturePlacementContext ctx, Random rand, BlockPos pos) {
		StructureWorldAccess level = ctx.getWorld();
		NoiseConfig randomState = level.toServerWorld().getChunkManager().getNoiseConfig();
		
		@Nullable
		GeneratorContext generatorContext;
		if((Object) randomState instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
			Cell cell = new Cell();
			generatorContext.lookup.apply(cell, pos.getX(), pos.getZ());
			return this.shouldPlace(cell, ctx, rand, pos);
		}
		return false;
	}
	
	protected abstract boolean shouldPlace(Cell cell, FeaturePlacementContext ctx, Random rand, BlockPos pos);
}
