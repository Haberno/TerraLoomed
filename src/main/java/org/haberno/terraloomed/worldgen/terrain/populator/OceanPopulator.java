package org.haberno.terraloomed.worldgen.terrain.populator;


import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.cell.CellPopulator;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.terrain.Terrain;

public record OceanPopulator(Terrain terrainType, Noise height) implements CellPopulator {

	@Override
	public void apply(Cell cell, float x, float z) {
		cell.terrain = this.terrainType;
		cell.height = Math.max(this.height.compute(x, z, 0), 0.0F);
		
		//TODO dont do this
		cell.erosion = -1.1F;
		cell.weirdness = -1.1F;
	}
}
