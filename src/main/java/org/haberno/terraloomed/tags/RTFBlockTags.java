package org.haberno.terraloomed.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.haberno.terraloomed.RTFCommon;

public class RTFBlockTags {
	public static final TagKey<Block> SOIL = resolve("soil");
	public static final TagKey<Block> ROCK = resolve("rock");
	public static final TagKey<Block> ORE_COMPATIBLE_ROCK = resolve("ore_compatible_rock");
	public static final TagKey<Block> CLAY = resolve("clay");
	public static final TagKey<Block> SEDIMENT = resolve("sediment");
	public static final TagKey<Block> ERODIBLE = resolve("erodible");
	
    private static TagKey<Block> resolve(String path) {
    	return TagKey.of(RegistryKeys.BLOCK, RTFCommon.location(path));
    }
}
