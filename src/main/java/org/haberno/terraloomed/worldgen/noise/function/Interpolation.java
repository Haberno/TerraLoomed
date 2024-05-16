package org.haberno.terraloomed.worldgen.noise.function;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import raccoonman.reterraforged.world.worldgen.noise.NoiseUtil;

public enum Interpolation implements CurveFunction, StringIdentifiable {
    LINEAR("LINEAR") {
    	
        @Override
        public float apply(float f) {
            return f;
        }
    }, 
    CURVE3("CURVE3") {
    	
        @Override
        public float apply(float f) {
        	return NoiseUtil.interpHermite(f);
        }
    }, 
    CURVE4("CURVE4") {
    	
        @Override
        public float apply(float f) {
        	return NoiseUtil.interpQuintic(f);
        }
    };
	
	public static final Codec<Interpolation> CODEC = StringIdentifiable.createCodec(Interpolation::values);
	
	private String name;
	
	private Interpolation(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return this.name;
	}
	
	@Override
	public Codec<Interpolation> codec()	{
		return CODEC;
	}
}
