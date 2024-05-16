package org.haberno.terraloomed.worldgen.surface;

import java.util.List;
import java.util.function.Function;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import raccoonman.reterraforged.world.worldgen.surface.rule.StrataRule.Strata;

public interface RTFSurfaceSystem {
	List<Strata> getOrCreateStrata(Identifier name, Function<Random, List<Strata>> factory);
}
