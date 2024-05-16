package org.haberno.terraloomed.worldgen.feature.placement.poisson;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.feature.placement.RTFPlacementModifiers;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.noise.module.Noises;
import org.haberno.terraloomed.worldgen.tile.Tile;

import java.util.Random;
import java.util.stream.Stream;

public class FastPoissonModifier extends PlacementModifier {
	public static final Codec<FastPoissonModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("radius").forGetter((p) -> p.radius),
		Codec.FLOAT.fieldOf("scale").forGetter((p) -> p.scale),
		Codec.FLOAT.fieldOf("jitter").forGetter((p) -> p.jitter),
		Codec.FLOAT.fieldOf("biome_fade").forGetter((p) -> p.biomeFade),
		Codec.INT.fieldOf("density_variance_scale").forGetter((p) -> p.densityVariationScale),
		Codec.FLOAT.fieldOf("density_variation").forGetter((p) -> p.densityVariation)
	).apply(instance, FastPoissonModifier::new));
	
	protected int radius;
	protected float scale;
	protected float jitter;
	protected float biomeFade;
	protected int densityVariationScale;
	protected float densityVariation;
	
	public FastPoissonModifier(
		int radius,
		float scale,
		float jitter,
		float biomeFade,
		int densityVariationScale,
		float densityVariation
	) {
		this.radius = radius;
		this.scale = scale;
		this.jitter = jitter;
		this.biomeFade = biomeFade;
		this.densityVariationScale = densityVariationScale;
		this.densityVariation = densityVariation;
	}
	
	@Override
	public Stream<BlockPos> getPositions(FeaturePlacementContext ctx, net.minecraft.util.math.random.Random random, BlockPos pos) {
		StructureWorldAccess level = ctx.getWorld();
		Chunk chunk = ctx.getWorld().getChunk(pos);
		long levelSeed = level.getSeed();
		int seed = (int) levelSeed + 234523;
		ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        FastPoisson poisson = FastPoisson.LOCAL_POISSON.get();
        DensityNoise density = this.getDensityNoise(seed, chunkPos, level.toServerWorld().getChunkManager().getNoiseConfig());
        FastPoissonContext poissonConfig = new FastPoissonContext(this.radius, this.jitter, this.scale, density);
        Stream.Builder<BlockPos> builder = Stream.builder();
        poisson.visit(seed, chunkX, chunkZ, new Random(levelSeed), poissonConfig, builder, (x, z, b) -> {
        	b.accept(new BlockPos(x, 0, z));
        });
        return builder.build();
    }

	@Override
	public PlacementModifierType<FastPoissonModifier> getType() {
		return RTFPlacementModifiers.FAST_POISSON;
	}

	private DensityNoise getDensityNoise(int seed, ChunkPos chunkPos, NoiseConfig randomState) {
		BiomeVariance biomeVariance = BiomeVariance.NONE;
		
		if (this.biomeFade > BiomeVariance.MIN_FADE) {
			if((Object) randomState instanceof RTFRandomState rtfRandomState) {
				Tile.Chunk reader = rtfRandomState.generatorContext().cache.provideAtChunk(chunkPos.x, chunkPos.z).getChunkReader(chunkPos.x, chunkPos.z);
				if (reader != null) {
					biomeVariance = new BiomeVariance(reader, this.biomeFade);
				}
			}
		}

		Noise densityVariance = Noises.one();
		if (this.densityVariation > 0) {
			densityVariance = Noises.simplex(seed + 1, this.densityVariationScale, 1);
			densityVariance = Noises.mul(densityVariance, this.densityVariation);
			densityVariance = Noises.add(densityVariance, 1.0F - this.densityVariation);
		}

		return new DensityNoise(biomeVariance, densityVariance);
	}
}
