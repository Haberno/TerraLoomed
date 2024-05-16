package org.haberno.terraloomed.worldgen.feature.chance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.tile.Tile;
import org.jetbrains.annotations.Nullable;

class ElevationChanceModifier extends RangeChanceModifier {
	public static final Codec<ElevationChanceModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.FLOAT.fieldOf("from").forGetter((o) -> o.from),
		Codec.FLOAT.fieldOf("to").forGetter((o) -> o.to),
		Codec.BOOL.fieldOf("exclusive").forGetter((o) -> o.exclusive)
	).apply(instance, ElevationChanceModifier::new));
	
	public ElevationChanceModifier(float from, float to, boolean exclusive) {
		super(from, to, exclusive);
	}

	@Override
	public Codec<ElevationChanceModifier> codec() {
		return CODEC;
	}

	@Override
	protected float getValue(ChanceContext chanceCtx, FeatureContext<?> placeCtx) {
		BlockPos pos = placeCtx.getOrigin();
		@Nullable
		GeneratorContext generatorContext;
		if((Object) placeCtx.getWorld().toServerWorld().getChunkManager().getNoiseConfig() instanceof RTFRandomState rtfRandomState && (generatorContext = rtfRandomState.generatorContext()) != null) {
			int x = pos.getX();
			int z = pos.getZ();
			int chunkX = ChunkSectionPos.getSectionCoord(x);
			int chunkZ = ChunkSectionPos.getSectionCoord(z);
			Tile.Chunk chunk = generatorContext.cache.provideAtChunk(chunkX, chunkZ).getChunkReader(chunkX, chunkZ);
			return rtfRandomState.generatorContext().localHeightmap.get().levels().elevation(chunk.getCell(x, z).height);
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
