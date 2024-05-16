package org.haberno.terraloomed.worldgen.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import raccoonman.reterraforged.world.worldgen.cell.Cell;

class MacroBiomeFilter extends CellFilter {
	public static final Codec<MacroBiomeFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.FLOAT.fieldOf("chance").forGetter((filter) -> filter.chance)
	).apply(instance, MacroBiomeFilter::new));

	private float chance;
	
	public MacroBiomeFilter(float chance) {
		this.chance = chance;
	}
	
	@Override
	protected boolean shouldPlace(Cell cell, FeaturePlacementContext ctx, Random rand, BlockPos pos) {
		return cell.macroBiomeId > (1.0F - this.chance);
	}
	
	@Override
	public PlacementModifierType<MacroBiomeFilter> getType() {
		return RTFPlacementModifiers.MACRO_BIOME_FILTER;
	}
}
