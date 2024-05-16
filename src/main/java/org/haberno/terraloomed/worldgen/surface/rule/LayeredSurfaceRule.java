package org.haberno.terraloomed.worldgen.surface.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRuleContext;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.worldgen.RTFRandomState;

public record LayeredSurfaceRule(TagKey<Layer> layers) implements MaterialRules.MaterialRule {
	public static final Codec<LayeredSurfaceRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TagKey.codec(RTFRegistries.SURFACE_LAYERS).fieldOf("layers").forGetter(LayeredSurfaceRule::layers)
	).apply(instance, LayeredSurfaceRule::new));
		
	@Override
	public MaterialRules.BlockStateRule apply(MaterialRuleContext ctx) {
		if((Object) ctx.noiseConfig instanceof RTFRandomState rtfRandomState) {
			Impl<Layer> layerLookup = rtfRandomState.registryAccess().getWrapperOrThrow(RTFRegistries.SURFACE_LAYERS);
			return MaterialRules.sequence(layerLookup.getOrThrow(this.layers).stream().map(Layer::unwrapRule).toArray(MaterialRules.MaterialRule[]::new)).apply(ctx);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public CodecHolder<LayeredSurfaceRule> codec() {
		return new CodecHolder<>(CODEC);
	}

	public static Layer layer(TagKey<Layer> layers) {
		return new Layer(RTFSurfaceRules.layered(layers));
	}
	
	public static Layer layer(MaterialRules.MaterialRule rule) {
		return new Layer(rule);
	}
	
	public record Layer(MaterialRules.MaterialRule rule) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			MaterialRules.MaterialRule.CODEC.fieldOf("rule").forGetter(Layer::rule)
		).apply(instance, Layer::new));
		
		protected static MaterialRules.MaterialRule unwrapRule(RegistryEntry<Layer> layer) {
			return layer.value().rule();
		}
	}
}
