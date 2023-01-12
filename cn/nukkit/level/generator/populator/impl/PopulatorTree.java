/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.tree.ObjectTree;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

public class PopulatorTree
extends PopulatorCount {
    private final int c;
    private ChunkManager d;

    public PopulatorTree() {
        this(0);
    }

    public PopulatorTree(int n) {
        this.c = n;
    }

    @Override
    public void populateCount(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3;
        this.d = chunkManager;
        int n4 = NukkitMath.randomRange(nukkitRandom, n << 4, (n << 4) + 15);
        int n5 = this.a(n4, n3 = NukkitMath.randomRange(nukkitRandom, n2 << 4, (n2 << 4) + 15));
        if (n5 < 3) {
            return;
        }
        ObjectTree.growTree(this.d, n4, n5, n3, nukkitRandom, this.c);
    }

    private int a(int n, int n2) {
        int n3;
        int n4;
        for (n4 = 254; n4 > 0 && (n3 = this.d.getBlockIdAt(n, n4, n2)) != 3 && n3 != 2; --n4) {
            if (n3 == 0 || n3 == 78) continue;
            return -1;
        }
        return ++n4;
    }
}

