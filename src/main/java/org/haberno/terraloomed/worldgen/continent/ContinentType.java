package org.haberno.terraloomed.worldgen.continent;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import org.haberno.terraloomed.worldgen.GeneratorContext;
import org.haberno.terraloomed.worldgen.continent.advanced.AdvancedContinentGenerator;
import org.haberno.terraloomed.worldgen.continent.fancy.FancyContinentGenerator;
import org.haberno.terraloomed.worldgen.continent.infinite.InfiniteContinentGenerator;
import org.haberno.terraloomed.worldgen.continent.simple.MultiContinentGenerator;
import org.haberno.terraloomed.worldgen.continent.simple.SingleContinentGenerator;
import org.haberno.terraloomed.worldgen.util.Seed;

public enum ContinentType implements StringIdentifiable {
    MULTI {
        
    	@Override
        public MultiContinentGenerator create(Seed seed, GeneratorContext context) {
            return new MultiContinentGenerator(seed, context);
        }
    }, 
    SINGLE {
        
    	@Override
        public SingleContinentGenerator create(Seed seed, GeneratorContext context) {
            return new SingleContinentGenerator(seed, context);
        }
    }, 
    MULTI_IMPROVED {
        
    	@Override
        public AdvancedContinentGenerator create(Seed seed, GeneratorContext context) {
            return new AdvancedContinentGenerator(seed, context);
        }
    }, 
    EXPERIMENTAL {
        
    	@Override
        public FancyContinentGenerator create(Seed seed, GeneratorContext context) {
            return new FancyContinentGenerator(seed, context);
        }
    },
    INFINITE {
        
    	@Override
        public InfiniteContinentGenerator create(Seed seed, GeneratorContext context) {
            return new InfiniteContinentGenerator(context);
        }
    };
	
	public static final Codec<ContinentType> CODEC = StringIdentifiable.createCodec(ContinentType::values);
    
    public abstract Continent create(Seed seed, GeneratorContext context);

    @Override
	public String asString() {
		return this.name();
	}
}
