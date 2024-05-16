package org.haberno.terraloomed.worldgen.feature.template.decorator;

import java.util.function.Function;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import com.mojang.serialization.Codec;
import raccoonman.reterraforged.registries.RTFBuiltInRegistries;
import raccoonman.reterraforged.world.worldgen.feature.template.template.TemplateContext;

public interface TemplateDecorator<T extends TemplateContext> {
    public static final Codec<TemplateDecorator<?>> CODEC = RTFBuiltInRegistries.TEMPLATE_DECORATOR_TYPE.byNameCodec().dispatch(TemplateDecorator::codec, Function.identity());
    
    void apply(WorldAccess level, T buffer, Random random, boolean modified);
    
    Codec<? extends TemplateDecorator<T>> codec();
}
