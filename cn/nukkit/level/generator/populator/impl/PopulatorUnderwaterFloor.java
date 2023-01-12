/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.PopulatorHelpers;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import java.util.List;

public class PopulatorUnderwaterFloor
extends PopulatorCount {
    private final double g;
    private final int e;
    private final int h;
    private final int f;
    private final int d;
    private final List<Integer> c;

    public PopulatorUnderwaterFloor(double d2, int n, int n2, int n3, int n4, List<Integer> list) {
        this.g = d2;
        this.e = n;
        this.h = n2;
        this.f = n3;
        this.d = n4;
        this.c = list;
    }

    @Override
    public void populateCount(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3;
        if (nukkitRandom.nextDouble() >= this.g) {
            return;
        }
        int n4 = (n << 4) + nukkitRandom.nextBoundedInt(16);
        int n5 = this.getHighestWorkableBlock(chunkManager, n4, n3 = (n2 << 4) + nukkitRandom.nextBoundedInt(16), fullChunk) - 1;
        if (n5 < this.d) {
            return;
        }
        if (chunkManager.getBlockIdAt(n4, n5 + 1, n3) != 9) {
            return;
        }
        int n6 = NukkitMath.randomRange(nukkitRandom, this.h, this.f);
        for (int k = n4 - n6; k <= n4 + n6; ++k) {
            for (int i2 = n3 - n6; i2 <= n3 + n6; ++i2) {
                if ((k - n4) * (k - n4) + (i2 - n3) * (i2 - n3) > n6 * n6) continue;
                for (int i3 = n5 - this.d; i3 <= n5 + this.d; ++i3) {
                    for (int n7 : this.c) {
                        if (chunkManager.getBlockIdAt(k, i3, i2) != n7) continue;
                        chunkManager.setBlockAt(k, i3, i2, this.e, 0);
                    }
                }
            }
        }
    }

    @Override
    protected int getHighestWorkableBlock(ChunkManager chunkManager, int n, int n2, FullChunk fullChunk) {
        int n3;
        n &= 0xF;
        n2 &= 0xF;
        for (n3 = 63; n3 >= 0 && PopulatorHelpers.isNonOceanSolid(fullChunk.getBlockId(n, n3, n2)); --n3) {
        }
        return n3 == 0 ? -1 : ++n3;
    }
}

