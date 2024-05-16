package org.haberno.terraloomed.worldgen.cell;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum CellField implements StringIdentifiable {
	HEIGHT("height") {
		
		@Override
		public float read(Cell cell) {
			return cell.height;
		}
	},
	CONTINENT_NOISE("continent_noise") {
		
		@Override
		public float read(Cell cell) {
			return cell.continentNoise;
		}
	},
	CONTINENTALNESS("continentalness") {
		
		@Override
		public float read(Cell cell) {
			return cell.continentalness;
		}
	},
	EROSION("erosion") {
		
		@Override
		public float read(Cell cell) {
			return cell.erosion;
		}
	},
	WEIRDNESS("weirdness") {
		
		@Override
		public float read(Cell cell) {
			return cell.weirdness;
		}
	},
	TERRAIN_REGION("terrain_region") {
		
		@Override
		public float read(Cell cell) {
			return cell.terrainRegionId;
		}
	},
	TERRAIN_REGION_EDGE("terrain_region_edge") {
		
		@Override
		public float read(Cell cell) {
			return cell.terrainRegionEdge;
		}
	},
	TERRAIN_MASK("terrain_mask") {
		
		@Override
		public float read(Cell cell) {
			return cell.terrainMask;
		}
	},
	RIVER_DISTANCE("river_distance") {
		
		@Override
		public float read(Cell cell) {
			return cell.riverDistance;
		}
	},
	BIOME_REGION("biome_region") {
		
		@Override
		public float read(Cell cell) {
			return cell.biomeRegionId;
		}
	},
	TEMPERATURE("temperature") {
		
		@Override
		public float read(Cell cell) {
			return cell.temperature;
		}
	},
	MOISTURE("moisture") {
		
		@Override
		public float read(Cell cell) {
			return cell.moisture;
		}
	},
	GRADIENT("gradient") {
		
		@Override
		public float read(Cell cell) {
			return cell.gradient;
		}
	},
	LOCAL_EROSION("local_erosion") {
		
		@Override
		public float read(Cell cell) {
			return cell.localErosion;
		}
	},
	SEDIMENT("sediment") {
		
		@Override
		public float read(Cell cell) {
			return cell.sediment;
		}
	};

	public static final Codec<CellField> CODEC = StringIdentifiable.createCodec(CellField::values);
	
	private String name;
	
	private CellField(String name) {
		this.name = name;
	}
	
	@Override
	public String asString() {
		return this.name;
	}
	
	public abstract float read(Cell cell);
}