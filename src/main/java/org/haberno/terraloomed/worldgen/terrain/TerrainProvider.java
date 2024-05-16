package org.haberno.terraloomed.worldgen.terrain;

import com.google.common.collect.ImmutableSet;
import org.haberno.terraloomed.data.preset.settings.TerrainSettings;
import org.haberno.terraloomed.worldgen.biome.Erosion;
import org.haberno.terraloomed.worldgen.cell.CellPopulator;
import org.haberno.terraloomed.worldgen.heightmap.Levels;
import org.haberno.terraloomed.worldgen.heightmap.RegionConfig;
import org.haberno.terraloomed.worldgen.noise.module.Noise;
import org.haberno.terraloomed.worldgen.noise.module.Noises;
import org.haberno.terraloomed.worldgen.terrain.populator.Populators;
import org.haberno.terraloomed.worldgen.terrain.populator.TerrainPopulator;
import org.haberno.terraloomed.worldgen.util.Seed;

import java.util.*;
import java.util.function.BiFunction;

public class TerrainProvider {
	
	@Deprecated
    public static List<CellPopulator> generateTerrain(Seed seed, TerrainSettings terrainSettings, RegionConfig config, Levels levels, Noise ground) {
    	TerrainSettings.General general = terrainSettings.general;
    	float verticalScale = general.globalVerticalScale;
    	boolean fancyMountains = general.fancyMountains;
    	Seed terrainSeed = seed.offset(general.terrainSeedOffset);
    	
    	List<TerrainPopulator> mixable = new ArrayList<>();
    	mixable.add(Populators.makeSteppe(terrainSeed, ground, terrainSettings.steppe));
    	mixable.add(Populators.makePlains(terrainSeed, ground, terrainSettings.plains, verticalScale));
        mixable.add(Populators.makeDales(terrainSeed, ground, terrainSettings.dales));
        mixable.add(Populators.makeHills1(terrainSeed, ground, terrainSettings.hills, verticalScale));
        mixable.add(Populators.makeHills2(terrainSeed, ground, terrainSettings.hills, verticalScale));
        mixable.add(Populators.makeTorridonian(terrainSeed, ground, terrainSettings.torridonian));
        mixable.add(Populators.makePlateau(terrainSeed, ground, terrainSettings.plateau, verticalScale));
        mixable.add(Populators.makeBadlands(terrainSeed, ground, terrainSettings.badlands));
    	mixable = mixable.stream().filter((populator) -> {
    		return populator.weight() > 0.0F;
    	}).toList();

        List<CellPopulator> unmixable = new ArrayList<>();
        unmixable.add(Populators.makeBadlands(terrainSeed, ground, terrainSettings.badlands));
        unmixable.add(Populators.makeMountains(terrainSeed, ground, terrainSettings.mountains, verticalScale, fancyMountains));
        unmixable.add(Populators.makeMountains2(terrainSeed, ground, terrainSettings.mountains, verticalScale, fancyMountains));
        unmixable.add(Populators.makeMountains3(terrainSeed, ground, terrainSettings.mountains, verticalScale, fancyMountains));
        unmixable.add(Populators.makeVolcano(terrainSeed, ground, config, levels, terrainSettings.volcano.weight));

        List<TerrainPopulator> mixed = combine(mixable, (t1, t2) -> {
        	return combine(ground, t1, t2, terrainSeed, levels, config.scale() / 2);
        });

        List<CellPopulator> result = new ArrayList<>();
        result.addAll(mixed);
        result.addAll(unmixable);
        
        Collections.shuffle(result, new Random(terrainSeed.next()));
        return result;
    }
	
	@Deprecated
	private static Set<Terrain> LOW_EROSION = ImmutableSet.of(
		TerrainType.PLATEAU, TerrainType.BADLANDS, TerrainType.TORRIDONIAN
	);
    
    private static TerrainPopulator combine(Noise ground, TerrainPopulator tp1, TerrainPopulator tp2, Seed seed, Levels levels, int scale) {
        Terrain type = TerrainType.registerComposite(tp1.type(), tp2.type());
        Noise selector = Noises.perlin(seed.next(), scale, 1);
        selector = Noises.warpPerlin(selector, seed.next(), scale / 2, 2, scale / 2.0F);

        Noise height = Noises.blend(selector, tp1.height(), tp2.height(), 0.5F, 0.25F);
        height = Noises.max(height, Noises.zero());
        
        Noise erosion = LOW_EROSION.contains(tp1.type()) && LOW_EROSION.contains(tp2.type()) ? Erosion.LEVEL_3.source() : Erosion.LEVEL_4.source();
        Noise weirdness = Noises.threshold(selector, tp1.weirdness(), tp2.weirdness(), 0.5F);

        float weight = (tp1.weight() + tp2.weight()) / 2.0F;
        return new TerrainPopulator(weight, type, ground, height, erosion, weirdness);
    }
    
    private static <T> List<T> combine(List<T> input, BiFunction<T, T, T> operator) {
        int length = input.size();
        for (int i = 1; i < input.size(); ++i) {
            length += input.size() - i;
        }
        List<T> result = new ArrayList<T>(length);
        for (int j = 0; j < length; ++j) {
            result.add(null);
        }
        int j = 0;
        int k = input.size();
        while (j < input.size()) {
            T t1 = input.get(j);
            result.set(j, t1);
            for (int l = j + 1; l < input.size(); ++l, ++k) {
                T t2 = input.get(l);
                T t3 = operator.apply(t1, t2);
                result.set(k, t3);
            }
            ++j;
        }
        return result;
    }
}
