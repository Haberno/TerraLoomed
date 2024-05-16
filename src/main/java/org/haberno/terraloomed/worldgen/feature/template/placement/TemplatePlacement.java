package org.haberno.terraloomed.worldgen.feature.template.placement;

import java.util.function.Function;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import com.mojang.serialization.Codec;
import raccoonman.reterraforged.registries.RTFBuiltInRegistries;
import raccoonman.reterraforged.world.worldgen.feature.template.template.Dimensions;
import raccoonman.reterraforged.world.worldgen.feature.template.template.TemplateContext;

public interface TemplatePlacement<T extends TemplateContext> {
    public static final Codec<TemplatePlacement<?>> CODEC = RTFBuiltInRegistries.TEMPLATE_PLACEMENT_TYPE.byNameCodec().dispatch(TemplatePlacement::codec, Function.identity());
    
    boolean canPlaceAt(WorldAccess world, BlockPos pos, Dimensions dimensions);

    boolean canReplaceAt(WorldAccess world, BlockPos pos);
    
    T createContext();
    
    Codec<? extends TemplatePlacement<T>> codec();
}
