/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

public class PopulatorGlowStone
extends Populator {
    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3;
        int n4 = NukkitMath.randomRange(nukkitRandom, n << 4, (n << 4) + 15);
        int n5 = PopulatorGlowStone.a(fullChunk, n4 & 0xF, (n3 = NukkitMath.randomRange(nukkitRandom, n2 << 4, (n2 << 4) + 15)) & 0xF);
        if (n5 != -1 && chunkManager.getBlockIdAt(n4, n5, n3) != 87) {
            int n6 = NukkitMath.randomRange(nukkitRandom, 40, 60);
            for (int k = 0; k < n6; ++k) {
                chunkManager.setBlockAt(n4 + (nukkitRandom.nextBoundedInt(7) - 3), n5 + (nukkitRandom.nextBoundedInt(9) - 4), n3 + (nukkitRandom.nextBoundedInt(7) - 3), 89);
            }
        }
    }

    private static int a(FullChunk fullChunk, int n, int n2) {
        int n3;
        int n4;
        for (n4 = 120; n4 >= 0 && (n3 = fullChunk.getBlockId(n, n4, n2)) != 0; --n4) {
        }
        return n4 == 0 ? -1 : n4;
    }
}

