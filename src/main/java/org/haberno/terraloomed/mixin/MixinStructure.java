package org.haberno.terraloomed.mixin;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structure.Context;
import net.minecraft.world.gen.structure.Structure.StructurePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import raccoonman.reterraforged.registries.RTFRegistries;
import raccoonman.reterraforged.world.worldgen.structure.rule.StructureRule;

@Mixin(Structure.class)
public class MixinStructure {

	@Inject(
		at = @At("HEAD"), 
		method = "isValidBiome",
		cancellable = true
	)
    private static void isValidBiome(StructurePosition generationStub, Context generationContext, CallbackInfoReturnable<Boolean> callback) {
		DynamicRegistryManager registry = generationContext.dynamicRegistryManager();
		Impl<StructureRule> structureRules = registry.getWrapperOrThrow(RTFRegistries.STRUCTURE_RULE);
		
		for(StructureRule structureRule : structureRules.listElements().map(RegistryEntry::value).toList()) {
			if(!structureRule.test(generationContext.noiseConfig(), generationStub.position())) {
				callback.setReturnValue(false);
			}
		}
    }
}
