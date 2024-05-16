package org.haberno.terraloomed.worldgen.biome;

import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import org.jetbrains.annotations.Nullable;

public class RTFBiomes {
    @Nullable
    private static final MusicSound NORMAL_MUSIC = null;

    public static Biome coldMarshland() {
    	//TODO add shrubbery and stuff
    	return null;
    }
    
    private static Biome biome(boolean hasPrecipitation, float skyColor, float downfall, SpawnSettings.Builder mobSpawnSettings, GenerationSettings.LookupBackedBuilder generationSettings, @Nullable MusicSound music) {
        return biome(hasPrecipitation, skyColor, downfall, 4159204, 329011, null, null, mobSpawnSettings, generationSettings, music);
    }

    private static Biome biome(boolean hasPrecipitation, float skyColor, float downfall, int waterColor, int waterFogColor, @Nullable Integer grassColorOverride, @Nullable Integer foliageColorOverride, SpawnSettings.Builder mobSpawnSettings, GenerationSettings.LookupBackedBuilder generationSettings, @Nullable MusicSound music) {
        BiomeEffects.Builder specialEffects = new BiomeEffects.Builder()
        	.waterColor(waterColor)
        	.waterFogColor(waterFogColor).fogColor(12638463).skyColor(calculateSkyColor(skyColor)).moodSound(BiomeMoodSound.CAVE).music(music);
        if (grassColorOverride != null) {
            specialEffects.grassColor(grassColorOverride);
        }
        if (foliageColorOverride != null) {
            specialEffects.foliageColor(foliageColorOverride);
        }
        return new Biome.Builder().precipitation(hasPrecipitation).temperature(skyColor).downfall(downfall).effects(specialEffects.build()).spawnSettings(mobSpawnSettings.build()).generationSettings(generationSettings.build()).build();
    }

    private static int calculateSkyColor(float f) {
        float g = f;
        g /= 3.0f;
        g = MathHelper.clamp(g, -1.0f, 1.0f);
        return MathHelper.hsvToRgb(0.62222224f - g * 0.05f, 0.5f + g * 0.1f, 1.0f);
    }
}
