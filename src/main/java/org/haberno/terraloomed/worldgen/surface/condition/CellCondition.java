package org.haberno.terraloomed.worldgen.surface.condition;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.HorizontalLazyAbstractPredicate;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.RTFRandomState;
import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.tile.Tile;
import org.haberno.terraloomed.worldgen.util.PosUtil;
import org.jetbrains.annotations.Nullable;

abstract class CellCondition extends HorizontalLazyAbstractPredicate {
	@Nullable
	private Tile.Chunk chunk;
	private long lastXZ;
	private boolean lastResult;
	
	@Nullable
	protected GeneratorContext generatorContext;
		
	public CellCondition(MaterialRules.MaterialRuleContext context) {
		super(context);
		//TODO store this in SurfaceRules$Context instead so we can cache the chunk lookup
		if((Object) context.noiseConfig instanceof RTFRandomState randomState && (this.generatorContext = randomState.generatorContext()) != null) {
			ChunkPos chunkPos = context.chunk.getPos();
			this.chunk = this.generatorContext.cache.provideChunk(chunkPos.x, chunkPos.z);
		}
		this.lastXZ = Long.MIN_VALUE;
	}
		
	public abstract boolean test(Cell cell, int x, int z);
	
	@Override
	public boolean test() {
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
