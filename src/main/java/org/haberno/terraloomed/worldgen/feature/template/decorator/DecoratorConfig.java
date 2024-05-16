package org.haberno.terraloomed.worldgen.feature.template.decorator;

import java.util.List;
import java.util.Map;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import raccoonman.reterraforged.world.worldgen.feature.template.template.TemplateContext;

public record DecoratorConfig<T extends TemplateContext>(List<TemplateDecorator<T>> defaultDecorators, Map<RegistryKey<Biome>, List<TemplateDecorator<T>>> biomeDecorators) {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Codec<DecoratorConfig<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		TemplateDecorator.CODEC.listOf().fieldOf("default_decorators").forGetter((c) -> (List) c.defaultDecorators()),
		Codec.unboundedMap(RegistryKey.createCodec(RegistryKeys.BIOME), TemplateDecorator.CODEC.listOf()).fieldOf("biome_decorators").forGetter((c) -> (Map) c.biomeDecorators())
	).apply(instance, (defaultDecorator, biomeDecorators) -> new DecoratorConfig(defaultDecorator, biomeDecorators)));
	
    public List<TemplateDecorator<T>> getDecorators(RegistryKey<Biome> biome) {
        if (biome == null) {
            return this.defaultDecorators;
        }
        return this.biomeDecorators.getOrDefault(biome, this.defaultDecorators);
    }
}
