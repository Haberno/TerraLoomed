package org.haberno.terraloomed.worldgen.feature.chance;

import net.minecraft.world.gen.feature.util.FeatureContext;

public abstract class RangeChanceModifier implements ChanceModifier {
	protected float from;
    protected float to;
	protected boolean exclusive;

    public RangeChanceModifier(float from, float to, boolean exclusive) {
        this.from = from;
        this.to = to;
        this.exclusive = exclusive;
    }
    
    protected abstract float getValue(ChanceContext chanceCtx, FeatureContext<?> placeCtx);
    
    @Override
	public float getChance(ChanceContext chanceCtx, FeatureContext<?> placeCtx) {
        return this.apply(this.getValue(chanceCtx, placeCtx));
    }

    private float apply(float value) {
    	float max = this.exclusive ? 0 : 1;;
    	float range = Math.abs(max - this.from);
        if (this.from < this.to) {
            if (value <= this.from) {
                return 0F;
            }
            if (value >= this.to) {
                return max;
            }
            return (value - this.from) / range;
        } else if (this.from > this.to) {
            if (value <= this.to) {
                return max;
            }
            if (value >= this.from) {
                return 0F;
            }
            return 1 - ((value - this.to) / range);
        }
        return 0F;
    }
}
