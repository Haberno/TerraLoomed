package org.haberno.terraloomed.worldgen.heightmap;

import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.WorldGenFlags;
import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.cell.noise.CellSampler;
import org.haberno.terraloomed.worldgen.terrain.TerrainType;
import org.haberno.terraloomed.worldgen.tile.Tile;
import org.haberno.terraloomed.worldgen.tile.TileCache;
import org.jetbrains.annotations.Nullable;

public class WorldLookup {
	private GeneratorContext context;
	private float waterLevel;
	private float beachLevel;
	private TileCache cache;
	private Levels levels;
	
	public WorldLookup(GeneratorContext context) {
		this.context = context;
		this.cache = context.cache;
		this.waterLevel = context.levels.water;
		this.beachLevel = context.levels.water(5);
		this.levels = context.levels;
	}

	public int getGenerationHeight(int chunkX, int chunkZ, ChunkGeneratorSettings generatorSettings, boolean load) {
		GenerationShapeConfig noiseSettings = generatorSettings.generationShapeConfig();
		
		int minY = noiseSettings.minimumY();
		int genHeight = noiseSettings.height();
		int cellHeight = noiseSettings.verticalCellBlockCount();
		
		if(!WorldGenFlags.cullNoiseSections()) {
			return genHeight;
		}
		
		@Nullable
		Tile tile = load ? this.cache.provideAtChunk(chunkX, chunkZ) : this.cache.provideAtChunkIfPresent(chunkX, chunkZ);
		if(tile != null) {
			Tile.Chunk chunk = tile.getChunkReader(chunkX, chunkZ);
			int generationHeight = Math.max(generatorSettings.seaLevel(), this.levels.scale(chunk.getHighestPoint()));
			return Math.min(genHeight, ((-minY + generationHeight) / cellHeight + 1) * cellHeight);
		} else {
			return genHeight;
		}
	}

	public boolean apply(Cell cell, int x, int z) {
		return this.apply(cell, x, z, false);
	}

	public boolean apply(Cell cell, int x, int z, boolean load) {
		if (load && this.computeAccurate(cell, x, z)) {
			return true;
		}
		if (this.computeCached(cell, x, z)) {
			return true;
		}
		return this.compute(cell, x, z);
	}

	private boolean computeAccurate(Cell cell, int x, int z) {
		int rx = this.cache.chunkToTile(x >> 4);
		int rz = this.cache.chunkToTile(z >> 4);
		Tile tile = this.cache.provide(rx, rz);
		Cell c = tile.lookup(x, z);
		if (c != null) {
			cell.copyFrom(c);
		}
		return cell.terrain != null;
	}

	private boolean computeCached(Cell cell, int x, int z) {
		int rx = this.cache.chunkToTile(x >> 4);
		int rz = this.cache.chunkToTile(z >> 4);
		Tile tile = this.cache.provideIfPresent(rx, rz);
		if (tile != null) {
			Cell c = tile.lookup(x, z);
			if (c != null) {
				cell.copyFrom(c);
			}
			return cell.terrain != null;
		}
		return false;
	}

	public boolean compute(Cell cell, int x, int z) {
		Heightmap heightmap = this.context.localHeightmap.get();
		CellSampler.Provider cellProvider = heightmap.cellProvider();
		
		@Nullable
		Cell oldCell = cellProvider.getCacheCell();
		
		cellProvider.setCacheCell(cell);
		heightmap.applyContinent(cell, x, z);
		heightmap.applyTerrain(cell, x, z, heightmap.continent().getRivermap(cell));
		heightmap.applyClimate(cell, x, z);
		if (cell.terrain == TerrainType.COAST && cell.height > this.waterLevel && cell.height <= this.beachLevel) {
			cell.terrain = TerrainType.BEACH;
		}
		heightmap.applyPost(cell, x, z);

		cellProvider.setCacheCell(oldCell);
		return false;
	}
}
