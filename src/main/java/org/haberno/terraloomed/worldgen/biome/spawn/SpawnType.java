package org.haberno.terraloomed.worldgen.biome.spawn;

import java.util.List;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.util.PosUtil;

public enum SpawnType implements StringIdentifiable {
    CONTINENT_CENTER("CONTINENT_CENTER") {
        private static final MultiNoiseUtil.ParameterRange FULL_RANGE = MultiNoiseUtil.ParameterRange.of(-1.0F, 1.0F);
        private static final MultiNoiseUtil.ParameterRange SURFACE_DEPTH = MultiNoiseUtil.ParameterRange.of(0.0F);
        private static final MultiNoiseUtil.ParameterRange INLAND_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(-0.11F, 0.55F);

		@Override
    	public BlockPos getSearchCenter(GeneratorContext ctx) {
    		long center = ctx.localHeightmap.get().continent().getNearestCenter(0.0F, 0.0F);
    		return new BlockPos(PosUtil.unpackLeft(center), 0, PosUtil.unpackRight(center));
    	}
    	
		@Override
		public List<NoiseHypercube> getParameterPoints() {
			return List.of(new NoiseHypercube(FULL_RANGE, FULL_RANGE, MultiNoiseUtil.ParameterRange.combine(INLAND_CONTINENTALNESS, FULL_RANGE), FULL_RANGE, SURFACE_DEPTH, FULL_RANGE, 0L), new NoiseHypercube(FULL_RANGE, FULL_RANGE, MultiNoiseUtil.ParameterRange.combine(INLAND_CONTINENTALNESS, FULL_RANGE), FULL_RANGE, SURFACE_DEPTH, FULL_RANGE, 0L));
		}
	}, 
    ISLANDS("ISLANDS") {

		@Override
		public BlockPos getSearchCenter(GeneratorContext ctx) {
			return BlockPos.ORIGIN;
		}

		@Override
		public List<NoiseHypercube> getParameterPoints() {
			return ImmutableList.of();
		}
	},
    WORLD_ORIGIN("WORLD_ORIGIN") {

		@Override
		public BlockPos getSearchCenter(GeneratorContext ctx) {
			return BlockPos.ORIGIN;
		}

		@Override
		public List<NoiseHypercube> getParameterPoints() {
			return ImmutableList.of();
		}
	};
	
	public static final Codec<SpawnType> CODEC = StringIdentifiable.createCodec(SpawnType::values);

	private String name;
	
	private SpawnType(String name) {
		this.name = name;
	}
	
	@Override
	public String asString() {
		return this.name;
	}
	
	public abstract BlockPos getSearchCenter(GeneratorContext ctx);
	
	public abstract List<NoiseHypercube> getParameterPoints();
}