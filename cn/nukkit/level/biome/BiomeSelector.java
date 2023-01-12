/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome;

import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.generator.noise.nukkit.f.SimplexF;
import cn.nukkit.math.NukkitRandom;

public class BiomeSelector {
    private final SimplexF e;
    private final SimplexF d;
    private final SimplexF a;
    private final SimplexF c;
    private final SimplexF b;

    public BiomeSelector(NukkitRandom nukkitRandom) {
        this.e = new SimplexF(nukkitRandom, 2.0f, 0.125f, 4.8828125E-4f);
        this.d = new SimplexF(nukkitRandom, 2.0f, 0.125f, 4.8828125E-4f);
        this.a = new SimplexF(nukkitRandom, 6.0f, 0.5f, 9.765625E-4f);
        this.c = new SimplexF(nukkitRandom, 6.0f, 0.5f, 4.8828125E-4f);
        this.b = new SimplexF(nukkitRandom, 2.0f, 0.5f, 4.8828125E-4f);
    }

    public Biome pickBiome(int n, int n2) {
        EnumBiome enumBiome;
        float f2 = this.c.noise2D(n, n2, true);
        float f3 = this.a.noise2D(n, n2, true);
        float f4 = this.e.noise2D(n, n2, true);
        float f5 = this.d.noise2D(n, n2, true);
        if (f2 < -0.15f) {
            enumBiome = f2 < -0.6f ? EnumBiome.MUSHROOM_ISLAND_SHORE : (f5 < 0.0f ? (f4 < -0.4f ? EnumBiome.FROZEN_OCEAN : EnumBiome.OCEAN) : EnumBiome.DEEP_OCEAN);
        } else if (Math.abs(f3) < 0.04f) {
            enumBiome = f4 < -0.4f ? EnumBiome.FROZEN_RIVER : EnumBiome.RIVER;
        } else {
            float f6 = this.b.noise2D(n, n2, true);
            enumBiome = f4 < -0.379f ? (f2 < -0.12f ? EnumBiome.COLD_BEACH : (f5 < 0.0f ? (f6 < -0.1f ? EnumBiome.COLD_TAIGA : (f6 < 0.5f ? EnumBiome.COLD_TAIGA_HILLS : EnumBiome.COLD_TAIGA_M)) : (f6 < 0.7f ? EnumBiome.ICE_PLAINS : EnumBiome.ICE_PLAINS_SPIKES))) : (f2 < -0.12f ? EnumBiome.BEACH : (f4 < 0.0f ? (f6 < 0.2f ? (f5 < -0.5f ? EnumBiome.EXTREME_HILLS_M : (f5 > 0.5f ? EnumBiome.EXTREME_HILLS_PLUS_M : (f5 < 0.0f ? EnumBiome.EXTREME_HILLS : EnumBiome.EXTREME_HILLS_PLUS))) : ((double)f5 < -0.6 ? EnumBiome.MEGA_TAIGA : ((double)f5 > 0.6 ? EnumBiome.MEGA_SPRUCE_TAIGA : (f5 < 0.2f ? EnumBiome.TAIGA : EnumBiome.TAIGA_M)))) : (f4 < 0.5f ? (f4 < 0.25f ? (f5 < 0.0f ? (f2 < 0.0f ? EnumBiome.SUNFLOWER_PLAINS : EnumBiome.PLAINS) : (f5 < 0.25f ? (f2 < 0.0f ? EnumBiome.FLOWER_FOREST : EnumBiome.FOREST) : (f2 < 0.0f ? EnumBiome.BIRCH_FOREST_M : EnumBiome.BIRCH_FOREST))) : (f5 < -0.2f ? (f2 < 0.0f ? EnumBiome.SWAMPLAND_M : EnumBiome.SWAMP) : (f5 > 0.1f ? (f2 < 0.155f ? EnumBiome.JUNGLE_M : EnumBiome.JUNGLE) : (f2 < 0.0f ? EnumBiome.ROOFED_FOREST_M : EnumBiome.ROOFED_FOREST)))) : (f5 < 0.0f ? (f2 < 0.0f ? EnumBiome.DESERT_M : (f6 < 0.0f ? EnumBiome.DESERT_HILLS : EnumBiome.DESERT)) : (f5 > 0.4f ? (f2 < 0.155f ? (f6 < 0.0f ? EnumBiome.SAVANNA_PLATEAU_M : EnumBiome.SAVANNA_M) : (f6 < 0.0f ? EnumBiome.SAVANNA_PLATEAU : EnumBiome.SAVANNA)) : (f2 < 0.0f ? (f6 < 0.0f ? EnumBiome.MESA_PLATEAU_F : EnumBiome.MESA_PLATEAU_F_M) : (f6 < 0.0f ? (f2 < 0.2f ? EnumBiome.MESA_PLATEAU_M : EnumBiome.MESA_PLATEAU) : (f2 < 0.1f ? EnumBiome.MESA_BRYCE : EnumBiome.MESA))))))));
        }
        return enumBiome.biome;
    }
}

