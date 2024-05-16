package org.haberno.terraloomed.worldgen.surface.rule;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.jetbrains.annotations.Nullable;
import Context;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import raccoonman.reterraforged.tags.RTFBlockTags;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;
import raccoonman.reterraforged.world.worldgen.heightmap.Levels;
import raccoonman.reterraforged.world.worldgen.noise.NoiseUtil;
import raccoonman.reterraforged.world.worldgen.noise.module.Noise;
import raccoonman.reterraforged.world.worldgen.noise.module.Noises;
import raccoonman.reterraforged.world.worldgen.surface.RTFSurfaceSystem;
import raccoonman.reterraforged.world.worldgen.surface.rule.StrataRule.Layer;
import raccoonman.reterraforged.world.worldgen.tile.Tile;
import raccoonman.reterraforged.world.worldgen.util.PosUtil;

public record StrataRule(Identifier cacheId, int buffer, int iterations, RegistryEntry<Noise> selector, List<org.haberno.map.worldgen.surface.rule.StrataRule.Layer> layers) implements MaterialRules.MaterialRule {
	public static final Codec<StrataRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Identifier.CODEC.fieldOf("cache_id").forGetter(StrataRule::cacheId),
		Codec.INT.fieldOf("buffer").forGetter(StrataRule::buffer),
		Codec.INT.fieldOf("iterations").forGetter(StrataRule::iterations),
		Noise.CODEC.fieldOf("selector").forGetter(StrataRule::selector),
		org.haberno.map.worldgen.surface.rule.StrataRule.Layer.CODEC.listOf().fieldOf("layers").forGetter(StrataRule::layers)
	).apply(instance, StrataRule::new));

	@Override
	public Rule apply(Context ctx) {
		if((Object) ctx.system instanceof RTFSurfaceSystem rtfSurfaceSystem) {
			return new Rule(ctx, rtfSurfaceSystem.getOrCreateStrata(this.cacheId, this::generate));
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public CodecHolder<StrataRule> codec() {
		return new CodecHolder<>(CODEC);
	}
	
	public List<Strata> generate(Random random) {
		List<Strata> strata = new ArrayList<>(this.iterations);
		for(int i = 0; i < this.iterations; i++) {
			strata.add(this.generateStrata(random));
		}
		return strata;
	}
	
	private Strata generateStrata(Random random) {
		List<Stratum> stratum = new ArrayList<>();
		for(org.haberno.map.worldgen.surface.rule.StrataRule.Layer layer : this.layers) {
			RegistryEntryList<Block> materials = Registries.BLOCK.getEntryList(layer.materials()).orElseThrow();
			int layerCount = layer.layers(random.nextFloat());
	        int lastIndex = -1;
	        for (int i = 0; i < layerCount; i++) {
	            int attempts = layer.attempts();
	            int index = random.nextInt(materials.size());
	            while (--attempts >= 0 && index == lastIndex) {
	                index = random.nextInt(materials.size());
	            }
	            if (index != lastIndex) {
	                lastIndex = index;
	                BlockState material = materials.get(index).value().getDefaultState();
	                float depth = layer.depth(random.nextFloat());
	                stratum.add(new Stratum(material, Noises.mul(Noises.perlin(random.nextInt(), 128, 3), depth)));
	            }
	        }
		}
		return new Strata(stratum);
	}
	
	public class Rule implements MaterialRules.BlockStateRule {
		private Context context;
		private List<Strata> strataEntries;
		private Levels levels;
		private Tile.Chunk chunk;
		private Strata strata;
		@Nullable
		private Stratum bufferMaterial;
		private int height;
		private int index;
		private float[] depthBuffer;
		private long lastXZ;
		
		public Rule(Context context, List<Strata> strataEntries) {
			this.context = context;
			this.strataEntries = strataEntries;
			
			if((Object) context.randomState instanceof RTFRandomState rtfRandomState) {
				ChunkPos chunkPos = context.chunk.getPos();
				
				GeneratorContext generatorContext = rtfRandomState.generatorContext();
				this.levels = generatorContext.levels;
				this.chunk = generatorContext.cache.provideChunk(chunkPos.x, chunkPos.z);
			} else {
				throw new IllegalStateException();
			}
		}
		
	    private Strata selectStrata(int x, int z) {
	    	float value = StrataRule.this.selector.value().compute(x, z, 0);
	        int index = (int)(value * this.strataEntries.size());
	        index = Math.min(this.strataEntries.size() - 1, index);
	        return this.strataEntries.get(index);
	    }

		private void update(int x, int z) {
			Strata strata = this.selectStrata(x, z);
			
			if(this.strata != strata) {
				this.strata = strata;
				
				for(Stratum stratum : this.strata.stratum()) {
					if(stratum.state().isIn(RTFBlockTags.ROCK)) {
						this.bufferMaterial = stratum;
						break;
					}	
				}
			}
			
			List<Stratum> stratum = this.strata.stratum();
			int stratumCount = stratum.size();
			
			if(this.depthBuffer == null || this.depthBuffer.length < stratumCount) {
				this.depthBuffer = new float[stratumCount];
			}

			float sum = 0.0F;
			for(int i = 0; i < stratumCount; i++) {
				float depth = stratum.get(i).depth().compute(x, z, 0);
				sum += depth;
				this.depthBuffer[i] = depth;
			}
			
			this.height = this.levels.scale(this.chunk.getCell(x, z).height);
			
			this.index = 0;
			
			int dy = this.height;
			int stoneStart = this.height - this.context.stoneDepthAbove + 1;
			for(int i = 0; i < stratumCount; i++) {
				this.depthBuffer[i] = dy -= NoiseUtil.round((this.depthBuffer[i] / sum) * this.height);
				
//				if(dy <= stoneStart && this.index == 0) {
//					this.index = i;
//				}
			}
		}
		
		@Override
		public BlockState tryApply(int x, int y, int z) {
			long packedPos = PosUtil.pack(x, z);
			if(this.lastXZ != packedPos) {
				this.update(x, z);
				this.lastXZ = packedPos;
			}
			
			if(StrataRule.this.buffer != 0 && y > this.height - StrataRule.this.buffer) {
				return this.bufferMaterial.state();
			}
			
			List<Stratum> stratum = this.strata.stratum;
			while(y < this.depthBuffer[this.index] && this.index + 1 < stratum.size()) {
				this.index++;
			}
			return stratum.get(this.index).state;
		}
	}
	
	public record Buffer(int size, TagKey<Block> materials) {
		public static final Codec<Buffer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("size").forGetter(Buffer::size),
			TagKey.codec(RegistryKeys.BLOCK).fieldOf("materials").forGetter(Buffer::materials)
		).apply(instance, Buffer::new));
	}
	
	public record Strata(List<Stratum> stratum) {
	}
	
	public record Stratum(BlockState state, Noise depth) {
	}
	
	public record Layer(TagKey<Block> materials, RegistryEntry<Noise> depth, int attempts, int minLayers, int maxLayers, float minDepth, float maxDepth) {
		public static final Codec<org.haberno.map.worldgen.surface.rule.StrataRule.Layer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TagKey.codec(RegistryKeys.BLOCK).fieldOf("materials").forGetter(org.haberno.map.worldgen.surface.rule.StrataRule.Layer::materials),
			Noise.CODEC.fieldOf("depth").forGetter(org.haberno.map.worldgen.surface.rule.StrataRule.Layer::depth),
			Codec.INT.fieldOf("attempts").forGetter(org.haberno.map.worldgen.surface.rule.StrataRule.Layer::attempts),
			Codec.INT.fieldOf("min_layers").forGetter(org.haberno.map.worldgen.surface.rule.StrataRule.Layer::minLayers),
			Codec.INT.fieldOf("max_layers").forGetter(org.haberno.map.worldgen.surface.rule.StrataRule.Layer::maxLayers),
			Codec.FLOAT.fieldOf("min_depth").forGetter(org.haberno.map.worldgen.surface.rule.StrataRule.Layer::minDepth),
			Codec.FLOAT.fieldOf("max_depth").forGetter(org.haberno.map.worldgen.surface.rule.StrataRule.Layer::maxDepth)
		).apply(instance, org.haberno.map.worldgen.surface.rule.StrataRule.Layer::new));
		
        public int layers(float f) {
            int range = this.maxLayers - this.minLayers;
            return this.minLayers + NoiseUtil.round(f * range);
        }
        
        public float depth(float f) {
            float range = this.maxDepth - this.minDepth;
            return this.minDepth + f * range;
        }
	}
}
