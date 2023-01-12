/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.biome.type.CoveredBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

public class PopulatorGroundCover
extends Populator {
    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        for (int k = 15; k >= 0; --k) {
            for (int i2 = 15; i2 >= 0; --i2) {
                Biome biome = EnumBiome.getBiome(fullChunk.getBiomeId(k, i2));
                if (!(biome instanceof CoveredBiome)) continue;
                ((CoveredBiome)biome).doCover(k, i2, fullChunk);
            }
        }
    }
}

