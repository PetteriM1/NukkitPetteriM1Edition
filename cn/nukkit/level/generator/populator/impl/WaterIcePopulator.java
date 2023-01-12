/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

public class WaterIcePopulator
extends Populator {
    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                int n3;
                Biome biome = EnumBiome.getBiome(fullChunk.getBiomeId(k, i2));
                if (!biome.isFreezing() || fullChunk.getBlockId(k, n3 = fullChunk.getHighestBlockAt(k, i2), i2) != 9) continue;
                fullChunk.setBlockId(k, n3, i2, 79);
            }
        }
    }
}

