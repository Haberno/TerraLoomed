package org.haberno.terraloomed.worldgen.surface.condition;

import java.util.List;
import java.util.Set;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import raccoonman.reterraforged.world.worldgen.cell.Cell;
import raccoonman.reterraforged.world.worldgen.terrain.Terrain;

class TerrainCondition extends CellCondition {
	private Set<Terrain> terrain;
	
	public TerrainCondition(Context context, Set<Terrain> terrain) {
		super(context);
		
		this.terrain = terrain;
	}

	@Override
	public boolean test(Cell cell, int x, int z) {
		return this.terrain.contains(cell.terrain);
	}
	
	public record Source(Set<Terrain> terrain) implements MaterialRules.MaterialCondition {
		public static final Codec<Source> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Terrain.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("terrain").forGetter(Source::terrain)
		).apply(instance, Source::new));

		@Override
		public TerrainCondition apply(Context ctx) {
			return new TerrainCondition(ctx, this.terrain);
		}

		@Override
		public CodecHolder<Source> codec() {
			return new CodecHolder<>(CODEC);
		}
	}
}
