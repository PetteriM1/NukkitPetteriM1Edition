/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

public class PopulatorNetherWart
extends Populator {
    private int b;
    private int a;

    public void setRandomAmount(int n) {
        this.b = n;
    }

    public void setBaseAmount(int n) {
        this.a = n;
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3 = nukkitRandom.nextRange(0, this.b + 1) + this.a;
        for (int k = 0; k < n3; ++k) {
            int n4;
            int n5 = nukkitRandom.nextRange(0, 15);
            int n6 = PopulatorNetherWart.a(fullChunk, n5, n4 = nukkitRandom.nextRange(0, 15));
            if (n6 == -1 || !PopulatorNetherWart.a(fullChunk, n5, n6, n4)) continue;
            fullChunk.setBlock(n5, n6, n4, 115);
        }
    }

    private static boolean a(FullChunk fullChunk, int n, int n2, int n3) {
        return fullChunk.getBlockId(n, n2, n3) == 0 && fullChunk.getBlockId(n, n2 - 1, n3) == 88;
    }

    private static int a(FullChunk fullChunk, int n, int n2) {
        int n3 = 0;
        for (int k = 0; k <= 127; ++k) {
            n3 = k;
            int n4 = fullChunk.getBlockId(n, k, n2);
            if (n4 == 0) break;
        }
        return n3 == 0 ? -1 : n3;
    }
}

