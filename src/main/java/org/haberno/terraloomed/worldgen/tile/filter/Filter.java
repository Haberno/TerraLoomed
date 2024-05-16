package org.haberno.terraloomed.worldgen.tile.filter;


import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.tile.Tile;

public interface Filter {
	void apply(Tile map, int regionX, int regionZ, int iterationsPerChunks);

	default void iterate(Tile map, Visitor visitor) {
		for (int dz = 0; dz < map.getBlockSize().total(); ++dz) {
			for (int dx = 0; dx < map.getBlockSize().total(); ++dx) {
				Cell cell = map.getCellRaw(dx, dz);
				visitor.visit(map, cell, dx, dz);
			}
		}
	}

	public interface Visitor {
		void visit(Tile map, Cell cell, int dx, int dz);
	}
}
