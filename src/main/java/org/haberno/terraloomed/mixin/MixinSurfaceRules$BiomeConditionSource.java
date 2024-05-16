package org.haberno.terraloomed.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.BiomeMaterialCondition;
import org.haberno.terraloomed.worldgen.surface.RTFSurfaceContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.function.Predicate;

@Mixin(BiomeMaterialCondition.class)
public class MixinSurfaceRules$BiomeConditionSource {
	@Shadow
	@Final
    Predicate<RegistryKey<Biome>> biomeNameTest;

    @Inject(at = @At("HEAD"), method = "apply", cancellable = true)
    public void apply(MaterialRules.MaterialRuleContext ctx, CallbackInfoReturnable<MaterialRules.BooleanSupplier> callback) {
    	Set<RegistryKey<Biome>> surroundingBiomes;
    	if((Object) ctx instanceof RTFSurfaceContext rtfSurfaceContext && (surroundingBiomes = rtfSurfaceContext.getSurroundingBiomes()) != null) {
    		boolean result = surroundingBiomes.stream().filter(this.biomeNameTest).findAny().isPresent();
    		if(!result || surroundingBiomes.size() == 1) {
    			callback.setReturnValue(() -> result);
    		}
    	}
    }
}