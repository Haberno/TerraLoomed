package org.haberno.terraloomed.worldgen.surface.condition;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.jetbrains.annotations.Nullable;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.RTFRandomState;
import raccoonman.reterraforged.world.worldgen.cell.Cell;
import raccoonman.reterraforged.world.worldgen.tile.Tile;
import raccoonman.reterraforged.world.worldgen.util.PosUtil;

abstract class CellCondition extends LazyXZCondition {
	@Nullable
	private Tile.Chunk chunk;
	private long lastXZ;
	private boolean lastResult;
	
	@Nullable
	protected GeneratorContext generatorContext;
		
	public CellCondition(MaterialRules.MaterialRuleContext context) {
		super(context);
		//TODO store this in SurfaceRules$Context instead so we can cache the chunk lookup
		if((Object) context.randomState instanceof RTFRandomState randomState && (this.generatorContext = randomState.generatorContext()) != null) {
			ChunkPos chunkPos = context.chunk.getPos();
			this.chunk = this.generatorContext.cache.provideChunk(chunkPos.x, chunkPos.z);
		}
		this.lastXZ = Long.MIN_VALUE;
	}
		
	public abstract boolean test(Cell cell, int x, int z);
	
	@Override
	public boolean compute() {
        int x = this.context.blockX;
        int z = this.context.blockZ;
        long packedPos = PosUtil.pack(x, z);
        if(this.lastXZ != packedPos && this.generatorContext != null) {
        	this.lastXZ = packedPos;
            Cell cell = this.chunk.getCell(x, z);
        	this.lastResult = this.test(cell, x, z);
        }
        return this.lastResult;
	}
}
