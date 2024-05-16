package org.haberno.terraloomed.worldgen.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.AbstractConditionalPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import java.util.List;

class DimensionFilter extends AbstractConditionalPlacementModifier {
	public static final Codec<DimensionFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		RegistryKey.createCodec(RegistryKeys.DIMENSION).listOf().fieldOf("blacklist").forGetter((filter) -> filter.blacklist)
	).apply(instance, DimensionFilter::new));
	
	private List<RegistryKey<DimensionOptions>> blacklist;
	private List<RegistryKey<World>> levelKeys;
	
	public DimensionFilter(List<RegistryKey<DimensionOptions>> blacklist) {
		this.blacklist = blacklist;
		this.levelKeys = this.blacklist.stream().map(RegistryKeys::toWorldKey).toList();
	}
	
	@Override
	protected boolean shouldPlace(FeaturePlacementContext ctx, Random rand, BlockPos pos) {
		StructureWorldAccess level = ctx.getWorld();
		MinecraftServer server = level.getServer();
				
		for(RegistryKey<World> key : this.levelKeys) {
			if(server.getWorld(key) == level.toServerWorld()) {
				return false;
			}
		}
		return true;
	}	

	@Override
	public PlacementModifierType<DimensionFilter> getType() {
		return RTFPlacementModifiers.DIMENSION_FILTER;
	}
}
