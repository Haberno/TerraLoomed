package org.haberno.terraloomed.worldgen.surface.condition;


import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRuleContext;
import org.haberno.terraloomed.worldgen.cell.Cell;
import org.haberno.terraloomed.worldgen.noise.module.Noise;

abstract class ThresholdCondition extends CellCondition {
	private Noise threshold;
	private Noise variance;
	
	public ThresholdCondition(MaterialRuleContext context, Noise threshold, Noise variance) {
		super(context);
		
		this.threshold = threshold;
		this.variance = variance;
	}

	@Override
	public boolean test(Cell cell, int x, int z) {
		return this.sample(cell) + this.variance.compute(x, z, 0) > this.threshold.compute(x, z, 0);
	}
	
	protected abstract float sample(Cell cell);
}
