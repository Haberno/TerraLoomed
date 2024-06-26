package org.haberno.terraloomed.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRuleContext;
import org.haberno.terraloomed.worldgen.surface.RTFSurfaceContext;
import org.haberno.terraloomed.worldgen.surface.SurfaceRegion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Implements(@Interface(iface = RTFSurfaceContext.class, prefix = "reterraforged$RTFSurfaceContext$"))
@Mixin(MaterialRuleContext.class)
abstract class MixinContext {
	@Shadow
	@Final
    public Chunk chunk;

	@Nullable
	private Set<RegistryKey<Biome>> surroundingBiomes;
	
	@Inject(at = @At("TAIL"), method = "<init>")
	private void Context(CallbackInfo callback) {
		ChunkRegion region = SurfaceRegion.get();
		
		if(region != null) {
			ChunkPos centerPos = this.chunk.getPos();
			
	    	this.surroundingBiomes = new HashSet<>();
	    	
	    	for(int x = -1; x <= 1; x++) {
	    		for(int z = -1; z <= 1; z++) {
	    			Chunk chunk = region.getChunk(centerPos.x + x, centerPos.z + z);
	    			
	    			for(ChunkSection section : chunk.getSectionArray()) {
	    				section.getBiomeContainer().forEachValue((biome) -> {
	    					biome.getKey().ifPresent(this.surroundingBiomes::add);
	    				});
	    			}
	        	}
	    	}
		}
	}

	public Set<RegistryKey<Biome>> reterraforged$RTFSurfaceContext$getSurroundingBiomes() {
		return this.surroundingBiomes;
	}
}
