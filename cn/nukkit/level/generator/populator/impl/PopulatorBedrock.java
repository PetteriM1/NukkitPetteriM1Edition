/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

public class PopulatorBedrock
extends Populator {
    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                fullChunk.setBlockId(k, 0, i2, 7);
                for (int i3 = 1; i3 < 5; ++i3) {
                    if (nukkitRandom.nextBoundedInt(i3) != 0) continue;
                    fullChunk.setBlockId(k, i3, i2, 7);
                }
            }
        }
    }
}

