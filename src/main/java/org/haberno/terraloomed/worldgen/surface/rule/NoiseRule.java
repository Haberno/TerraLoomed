package org.haberno.terraloomed.worldgen.surface.rule;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRuleContext;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.util.PosUtil;

import java.util.List;

record NoiseRule(RegistryEntry<Noise> noise, List<Pair<Float, MaterialRules.MaterialRule>> rules) implements MaterialRules.MaterialRule {
	public static final Codec<NoiseRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Noise.CODEC.fieldOf("noise").forGetter(NoiseRule::noise),
		entryCodec().listOf().fieldOf("rules").forGetter(NoiseRule::rules)
	).apply(instance, NoiseRule::new));
	
	@Override
	public Rule apply(MaterialRuleContext ctx) {
		return new Rule(this.noise.value(), this.rules.stream().map((pair) -> {
			return Pair.of(pair.getFirst(), pair.getSecond().apply(ctx));
		}).sorted((p1, p2) -> p2.getFirst().compareTo(p1.getFirst())).toList());
	}

	@Override
	public CodecHolder<NoiseRule> codec() {
		return new CodecHolder<>(CODEC);
	}
	
	private static Codec<Pair<Float, MaterialRules.MaterialRule>> entryCodec() {
		return RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("threshold").forGetter(Pair::getFirst),
			MaterialRules.MaterialRule.CODEC.fieldOf("rule").forGetter(Pair::getSecond)
		).apply(instance, Pair::new));
	}
	
	private static class Rule implements MaterialRules.BlockStateRule {
		private Noise noise;
		private List<Pair<Float, MaterialRules.BlockStateRule>> rules;
		private long lastPos;
		private MaterialRules.BlockStateRule rule;
		
		public Rule(Noise noise, List<Pair<Float, MaterialRules.BlockStateRule>> rules) {
			this.noise = noise;
			this.rules = rules;
			this.lastPos = Long.MIN_VALUE;
		}

		@Override
		public BlockState tryApply(int x, int y, int z) {
			long pos = PosUtil.pack(x, z);
			if(this.lastPos != pos) {
				float noise = this.noise.compute(x, z, 0);
				MaterialRules.BlockStateRule newRule = null;
				for(Pair<Float, MaterialRules.BlockStateRule> entry : this.rules) {
					if(noise > entry.getFirst()) {
						newRule = entry.getSecond();
						break;
					}
				}
				this.lastPos = pos;
				this.rule = newRule;
			}
			return this.rule != null ? this.rule.tryApply(x, y, z) : null;
		}
	}
}
