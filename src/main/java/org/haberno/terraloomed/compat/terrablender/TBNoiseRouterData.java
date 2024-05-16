package org.haberno.terraloomed.compat.terrablender;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import raccoonman.reterraforged.world.worldgen.cell.CellField;
import raccoonman.reterraforged.world.worldgen.densityfunction.RTFDensityFunctions;
import terrablender.core.TerraBlender;

public class TBNoiseRouterData {
	public static final RegistryKey<DensityFunction> UNIQUENESS = RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, new Identifier(TerraBlender.MOD_ID, "uniqueness"));
	
	public static void bootstrap(Registerable<DensityFunction> ctx) {
		ctx.register(UNIQUENESS, RTFDensityFunctions.cell(CellField.BIOME_REGION));
	}
}
