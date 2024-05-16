package org.haberno.terraloomed.data.preset;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import raccoonman.reterraforged.data.preset.settings.Preset;

public class PresetNoiseParameters {

	public static void bootstrap(Preset preset, Registerable<DoublePerlinNoiseSampler.NoiseParameters> ctx) {
//		TODO
//		CaveSettings caveSettings = preset.caves();
//		CaveSettings.Pillar pillars = caveSettings.pillars;
//		
//		double pillarRarenessModifier = 1.0D;
//		double pillarThicknessModifier = 1.0D;
//		double caveLayerModifier = 1.0D;
//		double caveCheeseModifier = 1.0D;
//		
//        register(ctx, Noises.PILLAR, -7, 1.0 * pillars.multiplier, 1.0 * pillars.multiplier);
//        register(ctx, Noises.PILLAR_RARENESS, -8, 1.0 * pillarRarenessModifier);
//        register(ctx, Noises.PILLAR_THICKNESS, -8, 1.0 * pillarThicknessModifier);
//        register(ctx, Noises.CAVE_LAYER, -8, 1.0 * caveLayerModifier);
//        register(ctx, Noises.CAVE_CHEESE, -8, 
//        	0.0 * caveCheeseModifier, 
//        	0.0 * caveCheeseModifier, 
//        	2.0 * caveCheeseModifier, 
//        	1.0 * caveCheeseModifier, 
//        	2.0 * caveCheeseModifier, 
//        	1.0 * caveCheeseModifier, 
//        	0.0 * caveCheeseModifier, 
//        	2.0 * caveCheeseModifier, 
//        	0.0 * caveCheeseModifier
//        );
	}

    private static void register(Registerable<DoublePerlinNoiseSampler.NoiseParameters> bootstapContext, RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> resourceKey, int firstOctave, double initialAmplitude, double ... amplitudes) {
        bootstapContext.register(resourceKey, new DoublePerlinNoiseSampler.NoiseParameters(firstOctave, initialAmplitude, amplitudes));
    }
}
