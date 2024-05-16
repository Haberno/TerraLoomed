package org.haberno.terraloomed.compat.terrablender;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import SurfaceRule;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import raccoonman.reterraforged.world.worldgen.surface.rule.RTFSurfaceRules;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;
import terrablender.worldgen.surface.NamespacedSurfaceRuleSource;

public class TBSurfaceRules {

	public static void bootstrap() {
		RTFSurfaceRules.register("terrablender", TBRule.CODEC);
	}
	
	public static TBRule rule(String category, String modId) {
		return new TBRule(category, modId);
	}
	
	private record TBRule(String category, String modId, Supplier<MaterialRules.MaterialRule> rules) implements MaterialRules.MaterialRule {
		private static final Supplier<Map<String, RuleCategory>> BY_KEY = Suppliers.memoize(() -> 
			Arrays.stream(RuleCategory.values()).collect(Collectors.toMap(RuleCategory::name, Function.identity()))
		);
		public static final Codec<TBRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("category").forGetter(TBRule::category),
			Codec.STRING.fieldOf("mod_id").forGetter(TBRule::modId)
		).apply(instance, TBRule::new));
		
		public TBRule(String categoryKey, String modId) {
			this(categoryKey, modId, Suppliers.memoize(() -> {
				RuleCategory category = BY_KEY.get().get(categoryKey);
				return SurfaceRuleManager.getNamespacedRules(category, SurfaceRuleManager.getDefaultSurfaceRules(category));
			}));
		}
		
		@Override
		public SurfaceRule apply(Context ctx) {
			if(this.rules.get() instanceof NamespacedSurfaceRuleSource ruleSource) {
				return ruleSource.sources().get(this.modId).apply(ctx);
			} else {
				throw new IllegalArgumentException(this.modId);
			}
		}

		@Override
		public CodecHolder<TBRule> codec() {
			return new CodecHolder<>(CODEC);
		}
	}
}
