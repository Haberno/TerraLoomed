package org.haberno.terraloomed.worldgen.surface.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRuleContext;
import org.haberno.terraloomed.platform.ModLoaderUtil;

record ModCondition(String modId) implements MaterialRules.MaterialCondition {
	public static final Codec<ModCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("mod_id").forGetter(ModCondition::modId)
	).apply(instance, ModCondition::new));
	
	@Override
	public MaterialRules.BooleanSupplier apply(MaterialRuleContext t) {
		boolean isModLoaded = ModLoaderUtil.isLoaded(this.modId);
		return () -> isModLoaded;
	}

	@Override
	public CodecHolder<ModCondition> codec() {
		return new CodecHolder<>(CODEC);
	}
}
