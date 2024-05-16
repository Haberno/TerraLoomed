package org.haberno.terraloomed.worldgen.feature.template.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.haberno.terraloomed.registries.RTFBuiltInRegistries;
import org.haberno.terraloomed.worldgen.feature.template.template.Dimensions;
import org.haberno.terraloomed.worldgen.feature.template.template.TemplateContext;

import java.util.function.Function;

public interface TemplatePlacement<T extends TemplateContext> {
    public static final Codec<TemplatePlacement<?>> CODEC = RTFBuiltInRegistries.TEMPLATE_PLACEMENT_TYPE.getCodec().dispatch(TemplatePlacement::codec, Function.identity());
    
    boolean canPlaceAt(WorldAccess world, BlockPos pos, Dimensions dimensions);

    boolean canReplaceAt(WorldAccess world, BlockPos pos);
    
    T createContext();
    
    Codec<? extends TemplatePlacement<T>> codec();
}
