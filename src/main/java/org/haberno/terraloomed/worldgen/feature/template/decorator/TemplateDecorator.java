package org.haberno.terraloomed.worldgen.feature.template.decorator;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;
import org.haberno.terraloomed.worldgen.feature.template.template.TemplateContext;

import java.util.function.Function;

public interface TemplateDecorator<T extends TemplateContext> {
    public static final Codec<TemplateDecorator<?>> CODEC = RTFBuiltInRegistries.TEMPLATE_DECORATOR_TYPE.getCodec().dispatch(TemplateDecorator::codec, Function.identity());
    
    void apply(WorldAccess level, T buffer, Random random, boolean modified);
    
    Codec<? extends TemplateDecorator<T>> codec();
}
