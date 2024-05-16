package org.haberno.terraloomed.worldgen.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.terrain.Terrain;

import java.util.List;
import java.util.Set;

class TerrainFilter extends CellFilter {
	public static final Codec<TerrainFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Terrain.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("terrain").forGetter((filter) -> filter.terrain),
		Codec.BOOL.fieldOf("exclude").forGetter((filter) -> filter.exclude)
	).apply(instance, TerrainFilter::new));
	
	private Set<Terrain> terrain;
	private boolean exclude;
	
	public TerrainFilter(Set<Terrain> terrain, boolean exclude) {
		this.terrain = terrain;
		this.exclude = exclude;
	}

	@Override
	protected boolean shouldPlace(Cell cell, FeaturePlacementContext ctx, Random rand, BlockPos pos) {
		boolean match = this.terrain.contains(cell.terrain);
		return this.exclude ? !match : match;
	}
	
	@Override
	public PlacementModifierType<TerrainFilter> getType() {
		return RTFPlacementModifiers.TERRAIN_FILTER;
	}
}
