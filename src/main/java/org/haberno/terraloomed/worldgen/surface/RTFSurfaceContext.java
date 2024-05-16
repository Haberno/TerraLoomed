package org.haberno.terraloomed.worldgen.surface;

import java.util.Set;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public interface RTFSurfaceContext {
	@Nullable
	Set<RegistryKey<Biome>> getSurroundingBiomes();
}
