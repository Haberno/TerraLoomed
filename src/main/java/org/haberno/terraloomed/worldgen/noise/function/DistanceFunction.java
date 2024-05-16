package org.haberno.terraloomed.worldgen.noise.function;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum DistanceFunction implements StringIdentifiable {
    EUCLIDEAN("EUCLIDEAN") {
        @Override
        public float apply(float x, float y) {
            return x * x + y * y;
        }
    }, 
    MANHATTAN("MANHATTAN") {
        @Override
        public float apply(float x, float y) {
            return Math.abs(x) + Math.abs(y);
        }
    }, 
    NATURAL("NATURAL") {
        @Override
        public float apply(float x, float y) {
            return Math.abs(x) + Math.abs(y) + (x * x + y * y);
        }
    };
	
	public static final Codec<DistanceFunction> CODEC = StringIdentifiable.createCodec(DistanceFunction::values);
	
	private String name;
	
	private DistanceFunction(String name) {
		this.name = name;
	}
    
	@Override
	public String asString() {
		return this.name;
	}
	
    public abstract float apply(float x, float z);
}
