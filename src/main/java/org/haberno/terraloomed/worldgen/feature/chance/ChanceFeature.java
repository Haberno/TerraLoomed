package org.haberno.terraloomed.worldgen.feature.chance;

import java.util.List;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.haberno.map.worldgen.feature.chance.ChanceFeature.Config;
import raccoonman.reterraforged.world.worldgen.feature.chance.ChanceFeature.Config;

public class ChanceFeature extends Feature<org.haberno.map.worldgen.feature.chance.ChanceFeature.Config> {

	public ChanceFeature(Codec<org.haberno.map.worldgen.feature.chance.ChanceFeature.Config> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<org.haberno.map.worldgen.feature.chance.ChanceFeature.Config> ctx) {
		Random random = ctx.getRandom();
		org.haberno.map.worldgen.feature.chance.ChanceFeature.Config config = ctx.getConfig();
		
		int entryCount = config.entries.size();
		ChanceContext chanceCtx = ChanceContext.make(entryCount);
        for (int i = 0; i < entryCount; i++) {
            Entry entry = config.entries.get(i);
            float chance = entry.getChance(chanceCtx, ctx);
            chanceCtx.record(i, chance);
        }

        int index = chanceCtx.nextIndex(random);
        if (index > -1) {
            return config.entries.get(index).feature.value().generateUnregistered(ctx.getWorld(), ctx.getGenerator(), random, ctx.getOrigin());
        }
		return false;
	}
	
	public record Entry(RegistryEntry<PlacedFeature> feature, float chance, List<ChanceModifier> modifiers) {
		public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PlacedFeature.REGISTRY_CODEC.fieldOf("feature").forGetter(Entry::feature),
			Codec.FLOAT.fieldOf("chance").forGetter(Entry::chance),
			ChanceModifier.CODEC.listOf().fieldOf("modifiers").forGetter(Entry::modifiers)
		).apply(instance, Entry::new));
		
		public float getChance(ChanceContext chanceCtx, FeatureContext<?> placeCtx) {
			float chance = this.chance;
			for (ChanceModifier modifier : this.modifiers) {
				chance *= modifier.getChance(chanceCtx, placeCtx);
			}
			return chance;
		}
	}
	
	public record Config(List<Entry> entries) implements FeatureConfig {
		public static final Codec<org.haberno.map.worldgen.feature.chance.ChanceFeature.Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Entry.CODEC.listOf().fieldOf("entries").forGetter(org.haberno.map.worldgen.feature.chance.ChanceFeature.Config::entries)
		).apply(instance, org.haberno.map.worldgen.feature.chance.ChanceFeature.Config::new));
	}
}
