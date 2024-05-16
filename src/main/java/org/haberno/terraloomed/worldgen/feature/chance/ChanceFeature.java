package org.haberno.terraloomed.worldgen.feature.chance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.haberno.terraloomed.worldgen.feature.chance.ChanceFeature.Config;

import java.util.List;

public class ChanceFeature extends Feature<Config> {

	public ChanceFeature(Codec<Config> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<Config> ctx) {
		Random random = ctx.getRandom();
		Config config = ctx.getConfig();
		
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
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Entry.CODEC.listOf().fieldOf("entries").forGetter(Config::entries)
		).apply(instance, Config::new));
	}
}
