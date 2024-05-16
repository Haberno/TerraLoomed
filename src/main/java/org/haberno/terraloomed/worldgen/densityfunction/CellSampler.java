package org.haberno.terraloomed.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.biome.source.BiomeCoords;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.cell.CellField;
import org.haberno.terraloomed.worldgen.heightmap.WorldLookup;
import org.haberno.terraloomed.worldgen.tile.Tile;
import org.haberno.terraloomed.worldgen.util.PosUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record CellSampler(Supplier<GeneratorContext> generatorContext, CellField field) implements MappedFunction {
	private static final ThreadLocal<Cache2d> LOCAL_CELL = ThreadLocal.withInitial(Cache2d::new);
	
	@Override
	public double sample(NoisePos ctx) {
		WorldLookup worldLookup = this.generatorContext.get().lookup;
		Cell cell = LOCAL_CELL.get().getAndUpdate(worldLookup, ctx.blockX(), ctx.blockZ());
		return this.field.read(cell);
	}

	@Override
	public double minValue() {
		return 0.0F;
	}

	@Override
	public double maxValue() {
		return 1.0F;
	}

	public static boolean isCachedNoiseChunk(int cellCountXZ) {
		return cellCountXZ > 1;
	}
	
	public static class Cache2d {
		private long lastPos = Long.MAX_VALUE;
		private Cell cell = new Cell();
		
		public Cell getAndUpdate(WorldLookup lookup, int blockX, int blockZ) {
			blockX = BiomeCoords.toBlock(BiomeCoords.fromBlock(blockX));
			blockZ = BiomeCoords.toBlock(BiomeCoords.fromBlock(blockZ));
			
			long packedPos = PosUtil.pack(blockX, blockZ);
			if(this.lastPos != packedPos) {
				lookup.apply(this.cell.reset(), blockX, blockZ);
				this.lastPos = packedPos;
			}
			return this.cell;
		}
	}
	
	public class CacheChunk implements MappedFunction {
		@Nullable
		private Tile.Chunk chunk;
		private Cache2d cache2d;
		private int chunkX, chunkZ;

		public CacheChunk(@Nullable Tile.Chunk chunk, Cache2d cache2d, int chunkX, int chunkZ) {
			this.chunk = chunk;
			this.cache2d = cache2d;
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
		}

		@Override
		public double sample(NoisePos ctx) {
			int blockX = ctx.blockX();
			int blockZ = ctx.blockZ();
			int chunkX = ChunkSectionPos.getSectionCoord(blockX);
			int chunkZ = ChunkSectionPos.getSectionCoord(blockZ);
			int quartBlockX = BiomeCoords.toBlock(BiomeCoords.fromBlock(blockX));
			int quartBlockZ = BiomeCoords.toBlock(BiomeCoords.fromBlock(blockZ));
			
			WorldLookup worldLookup = CellSampler.this.generatorContext.get().lookup;
			Cell cell = (this.chunk != null && this.chunkX == chunkX && this.chunkZ == chunkZ) ? 
				this.chunk.getCell(blockX, blockZ) :
				this.cache2d.getAndUpdate(worldLookup, quartBlockX, quartBlockZ);
			return CellSampler.this.field.read(cell);
		}

		@Override
		public double minValue() {
			return CellSampler.this.minValue();
		}

		@Override
		public double maxValue() {
			return CellSampler.this.maxValue();
		}
	}
	
	public record Marker(CellField field) implements MappedFunction.Marker {
		public static final Codec<Marker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				CellField.CODEC.fieldOf("field").forGetter(Marker::field)
		).apply(instance, Marker::new));
		
		@Override
		public CodecHolder<Marker> getCodecHolder() {
			return new CodecHolder<>(CODEC);
		}
	}
}