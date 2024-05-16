package org.haberno.terraloomed.worldgen.feature.template;

import java.util.List;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.haberno.map.worldgen.feature.template.TemplateFeature.Config;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.server.RTFMinecraftServer;
import raccoonman.reterraforged.world.worldgen.feature.template.TemplateFeature.Config;
import raccoonman.reterraforged.world.worldgen.feature.template.decorator.DecoratorConfig;
import raccoonman.reterraforged.world.worldgen.feature.template.decorator.TemplateDecorator;
import raccoonman.reterraforged.world.worldgen.feature.template.paste.Paste;
import raccoonman.reterraforged.world.worldgen.feature.template.paste.PasteConfig;
import raccoonman.reterraforged.world.worldgen.feature.template.paste.PasteType;
import raccoonman.reterraforged.world.worldgen.feature.template.placement.TemplatePlacement;
import raccoonman.reterraforged.world.worldgen.feature.template.template.Dimensions;
import raccoonman.reterraforged.world.worldgen.feature.template.template.FeatureTemplate;
import raccoonman.reterraforged.world.worldgen.feature.template.template.TemplateContext;

public class TemplateFeature extends Feature<org.haberno.map.worldgen.feature.template.TemplateFeature.Config<?>> {

	public TemplateFeature(Codec<org.haberno.map.worldgen.feature.template.TemplateFeature.Config<?>> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<org.haberno.map.worldgen.feature.template.TemplateFeature.Config<?>> ctx) {
		Random random = ctx.getRandom();
		org.haberno.map.worldgen.feature.template.TemplateFeature.Config<?> config = ctx.getConfig();
		
		BlockMirror mirror = nextMirror(random);
		BlockRotation rotation = nextRotation(random);
        return paste(ctx.getWorld(), random, ctx.getOrigin(), mirror, rotation, config, FeatureTemplate.WORLD_GEN);
	}

    public static <T extends TemplateContext> boolean paste(StructureWorldAccess world, Random rand, BlockPos pos, BlockMirror mirror, BlockRotation rotation, org.haberno.map.worldgen.feature.template.TemplateFeature.Config<T> config, PasteType pasteType) {
        return paste(world, rand, pos, mirror, rotation, config, pasteType, false);
    }

    public static <T extends TemplateContext> boolean paste(StructureWorldAccess world, Random rand, BlockPos pos, BlockMirror mirror, BlockRotation rotation, org.haberno.map.worldgen.feature.template.TemplateFeature.Config<T> config, PasteType pasteType, boolean modified) {
        if (config.templates().isEmpty()) {
            RTFCommon.LOGGER.warn("Empty template list for config");
            return false;
        }
        
        if(world.getServer() instanceof RTFMinecraftServer rtfMinecraftServer) {
	        DecoratorConfig<T> decoratorConfig = config.decorator();
	        
	        Identifier templateName = nextTemplate(config.templates, rand);
	        FeatureTemplate template = rtfMinecraftServer.getFeatureTemplateManager().load(templateName);
	        
	        Dimensions dimensions = template.getDimensions(mirror, rotation);
	        TemplatePlacement<T> placement = config.placement();
	        if (!placement.canPlaceAt(world, pos, dimensions)) {
	            return false;
	        }
	
	        Paste paste = pasteType.get(template);
	        T buffer = placement.createContext();
	        if (paste.apply(world, buffer, pos, mirror, rotation, placement, config.paste())) {
	            RegistryKey<Biome> biome = world.getBiome(pos).getKey().orElse(null);
	            for (TemplateDecorator<T> decorator : decoratorConfig.getDecorators(biome)) {
	                decorator.apply(world, buffer, rand, modified);
	            }
	            return true;
	        }
	
	        return false;
        } else {
        	throw new IllegalStateException();
        }
    }

	private static Identifier nextTemplate(List<Identifier> templates, Random random) {
        return templates.get(random.nextInt(templates.size()));
    }

    private static BlockMirror nextMirror(Random random) {
        return BlockMirror.values()[random.nextInt(BlockMirror.values().length)];
    }

    private static BlockRotation nextRotation(Random random) {
        return BlockRotation.values()[random.nextInt(BlockRotation.values().length)];
    }
    
	public record Config<T extends TemplateContext>(List<Identifier> templates, TemplatePlacement<T> placement, PasteConfig paste, DecoratorConfig<T> decorator) implements FeatureConfig {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static final Codec<org.haberno.map.worldgen.feature.template.TemplateFeature.Config<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.listOf().fieldOf("templates").forGetter(org.haberno.map.worldgen.feature.template.TemplateFeature.Config::templates),
			TemplatePlacement.CODEC.fieldOf("placement").forGetter(org.haberno.map.worldgen.feature.template.TemplateFeature.Config::placement),
			PasteConfig.CODEC.fieldOf("paste").forGetter(org.haberno.map.worldgen.feature.template.TemplateFeature.Config::paste),
			DecoratorConfig.CODEC.fieldOf("decorator").forGetter(org.haberno.map.worldgen.feature.template.TemplateFeature.Config::decorator)
		).apply(instance, (templates, placement, paste, decorator) -> new Config(templates, placement, paste, decorator)));
	}
}
