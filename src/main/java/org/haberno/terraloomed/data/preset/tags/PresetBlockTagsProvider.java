package org.haberno.terraloomed.data.preset.tags;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.ValueLookupTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import org.haberno.terraloomed.tags.RTFBlockTags;

import java.util.concurrent.CompletableFuture;

public class PresetBlockTagsProvider extends ValueLookupTagProvider<Block> {
	
	public PresetBlockTagsProvider(DataOutput packOutput, CompletableFuture<WrapperLookup> completableFuture) {
		super(packOutput, RegistryKeys.BLOCK, completableFuture, (block) -> block.getRegistryEntry().registryKey());
	}

	@Override
	protected void configure(WrapperLookup provider) {
		this.getOrCreateTagBuilder(RTFBlockTags.SOIL).add(Blocks.DIRT, Blocks.COARSE_DIRT);
		this.getOrCreateTagBuilder(RTFBlockTags.CLAY).add(Blocks.CLAY);
		this.getOrCreateTagBuilder(RTFBlockTags.SEDIMENT).add(Blocks.SAND, Blocks.GRAVEL);
		this.getOrCreateTagBuilder(RTFBlockTags.ERODIBLE).add(Blocks.SNOW_BLOCK).add(Blocks.POWDER_SNOW).add(Blocks.PACKED_ICE).add(Blocks.GRAVEL).addOptionalTag(BlockTags.DIRT.id());

		Block[] rocks = { Blocks.GRANITE, Blocks.ANDESITE, Blocks.STONE, Blocks.DIORITE };
		this.getOrCreateTagBuilder(RTFBlockTags.ROCK).add(rocks);
		this.getOrCreateTagBuilder(RTFBlockTags.ORE_COMPATIBLE_ROCK).add(rocks);
		this.getOrCreateTagBuilder(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(Blocks.CLAY);
	}
}