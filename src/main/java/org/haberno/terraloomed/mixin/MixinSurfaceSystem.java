package org.haberno.terraloomed.mixin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import raccoonman.reterraforged.RTFCommon;
import raccoonman.reterraforged.compat.terrablender.TBCompat;
import raccoonman.reterraforged.world.worldgen.surface.RTFSurfaceSystem;
import raccoonman.reterraforged.world.worldgen.surface.rule.StrataRule.Strata;
import terrablender.worldgen.surface.NamespacedSurfaceRuleSource;

@Mixin(SurfaceBuilder.class)
@Implements(@Interface(iface = RTFSurfaceSystem.class, prefix = RTFCommon.MOD_ID + "$RTFSurfaceSystem$"))
class MixinSurfaceSystem {
	private static final Identifier STRATA_RANDOM = RTFCommon.location("strata");
	private Random strataRandom;
	private Map<Identifier, List<Strata>> strata;
	
	@Inject(
		at = @At("TAIL"),
		method = "<init>"
	)
    public void SurfaceSystem(NoiseConfig randomState, BlockState blockState, int i, RandomSplitter positionalRandomFactory, CallbackInfo callback) {
    	this.strataRandom = randomState.randomDeriver.fromHashOf(STRATA_RANDOM);
    	this.strata = new ConcurrentHashMap<>();
	}
	
	@ModifyVariable(
		at = @At("HEAD"),
		method = "buildSurface",
		name = "ruleSource",
		ordinal = 0,
		index = 7,
		argsOnly = true
	)
	public MaterialRules.MaterialRule buildSurface(MaterialRules.MaterialRule source) {
		// let our own surface api handle this instead
//		if(TBIntegration.isEnabled() && source instanceof NamespacedSurfaceRuleSource namespacedRule) {
//			return namespacedRule.base();
//		}	
		return source;
	}

	public List<Strata> reterraforged$RTFSurfaceSystem$getOrCreateStrata(Identifier cacheId, Function<Random, List<Strata>> factory) {
		return this.strata.computeIfAbsent(cacheId, (k) -> {
			return factory.apply(this.strataRandom.fork());
		});
	}
}
