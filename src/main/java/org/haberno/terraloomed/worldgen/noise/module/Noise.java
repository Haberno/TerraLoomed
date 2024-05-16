package org.haberno.terraloomed.worldgen.noise.module;

import com.mojang.serialization.Codec;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import raccoonman.reterraforged.registries.RTFRegistries;

public interface Noise {
    public static final Codec<Noise> DIRECT_CODEC = Noises.DIRECT_CODEC;
    public static final Codec<RegistryEntry<Noise>> CODEC = RegistryElementCodec.of(RTFRegistries.NOISE, DIRECT_CODEC);
    public static final Codec<Noise> HOLDER_HELPER_CODEC = CODEC.xmap(Noises.HolderHolder::new, noise -> {
        if (noise instanceof Noises.HolderHolder holderHolder) {
            return holderHolder.holder();
        }
        return new RegistryEntry.Direct<>(noise);
    });
    
	float compute(float x, float z, int seed);
	
	float minValue();
	
	float maxValue();
	
	Noise mapAll(Visitor visitor);
	
	Codec<? extends Noise> codec();
	
	public interface Visitor {
		Noise apply(Noise input);
	}
}
