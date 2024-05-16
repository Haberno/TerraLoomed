package org.haberno.terraloomed.worldgen.surface.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.cell.Cell;

public class HeightModificationDetection extends CellCondition {
	private Target target;
	
	private HeightModificationDetection(Context context, Target target) {
		super(context);
		
		this.target = target;
	}

	@Override
	public boolean test(Cell cell, int blockX, int blockZ) {
		return this.target.test(cell, blockX, blockZ, this.context, this.generatorContext);
	}

	public record Source(Target target) implements MaterialRules.MaterialCondition {
		public static final Codec<Source> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Target.CODEC.fieldOf("target").forGetter(Source::target)
		).apply(instance, Source::new));
		
		@Override
		public HeightModificationDetection apply(Context ctx) {
			return new HeightModificationDetection(ctx, this.target);
		}

		@Override
		public CodecHolder<Source> codec() {
			return new CodecHolder<>(CODEC);
		}
	}
	
	public enum Target implements StringIdentifiable {
		STRUCTURE_BEARDIFIER("structure_beardifier") {
			
			@Override
			public boolean test(Cell cell, int blockX, int blockZ, Context surfaceContext, GeneratorContext generatorContext) {
				return surfaceContext.blockY == surfaceContext.chunk.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockX & 0xF, blockZ & 0xF) && surfaceContext.blockY != generatorContext.levels.scale(cell.height);
			}
		};

		public static final Codec<Target> CODEC = StringIdentifiable.createCodec(Target::values);
		
		private String name;
		
		private Target(String name) {
			this.name = name;
		}
		
		@Override
		public String asString() {
			return this.name;
		}
		
		public abstract boolean test(Cell cell, int blockX, int blockZ, Context surfaceContext, GeneratorContext generatorContext);
	}
}
