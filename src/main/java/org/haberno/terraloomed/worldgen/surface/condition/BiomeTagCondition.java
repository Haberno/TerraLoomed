package org.haberno.terraloomed.worldgen.surface.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;

record BiomeTagCondition(TagKey<Biome> tag) implements MaterialRules.MaterialCondition {
	public static final Codec<BiomeTagCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TagKey.codec(RegistryKeys.BIOME).fieldOf("tag").forGetter(BiomeTagCondition::tag)
	).apply(instance, BiomeTagCondition::new));
	
	@SuppressWarnings("unchecked")
	@Override
	public MaterialRules.BooleanSupplier apply(Context ctx) {
		if((Object) ctx.randomState instanceof RTFRandomState rtfRandomState) {
			Impl<Biome> registry = rtfRandomState.registryAccess().lookupOrThrow(RegistryKeys.BIOME);
			return MaterialRules.biome(registry.getOrThrow(this.tag)
				.stream()
				.map((holder) -> holder.getKey().orElseThrow())
				.toArray(RegistryKey[]::new)
			).apply(ctx);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public CodecHolder<BiomeTagCondition> codec() {
		return new CodecHolder<>(CODEC);
	}
}
