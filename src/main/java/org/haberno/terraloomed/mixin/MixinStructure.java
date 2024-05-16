package org.haberno.terraloomed.mixin;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structure.Context;
import net.minecraft.world.gen.structure.Structure.StructurePosition;
import org.haberno.terraloomed.registries.RTFRegistries;
import org.haberno.terraloomed.worldgen.structure.rule.StructureRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Structure.class)
public class MixinStructure {

	@Inject(
		at = @At("HEAD"), 
		method = "isBiomeValid",
		cancellable = true
	)
    private static void isBiomeValid(StructurePosition generationStub, Context generationContext, CallbackInfoReturnable<Boolean> callback) {
		DynamicRegistryManager registry = generationContext.dynamicRegistryManager();
		Impl<StructureRule> structureRules = registry.getWrapperOrThrow(RTFRegistries.STRUCTURE_RULE);
		
		for(StructureRule structureRule : structureRules.streamEntries().map(RegistryEntry::value).toList()) {
			if(!structureRule.test(generationContext.noiseConfig(), generationStub.position())) {
				callback.setReturnValue(false);
			}
		}
    }
}
