package org.haberno.terraloomed.worldgen.surface;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.haberno.terraloomed.worldgen.surface.rule.StrataRule.Strata;

import java.util.List;
import java.util.function.Function;

public interface RTFSurfaceSystem {
	List<Strata> getOrCreateStrata(Identifier name, Function<Random, List<Strata>> factory);
}
