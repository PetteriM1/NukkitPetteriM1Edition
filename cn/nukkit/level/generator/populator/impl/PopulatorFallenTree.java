/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.structure.ObjectFallenTree;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

public class PopulatorFallenTree
extends Populator {
    private ChunkManager a;
    private int b;

    public void setType(int n) {
        this.b = n;
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        if (nukkitRandom.nextRange(0, 3) != 1) {
            return;
        }
        this.a = chunkManager;
        int n3 = 1;
        for (int k = 0; k < n3; ++k) {
            int n4;
            int n5 = NukkitMath.randomRange(nukkitRandom, n << 4, (n << 4) + 15);
            int n6 = this.a(n5, n4 = NukkitMath.randomRange(nukkitRandom, n2 << 4, (n2 << 4) + 15));
            if (n6 == -1 || chunkManager.getBlockIdAt(n5, n6, n4) != 0 || chunkManager.getBlockIdAt(n5 + 1, n6, n4) != 0 || chunkManager.getBlockIdAt(n5, n6, n4 + 1) != 0 || chunkManager.getBlockIdAt(n5 - 1, n6, n4) != 0 || chunkManager.getBlockIdAt(n5, n6, n4 - 1) != 0 || chunkManager.getBlockIdAt(n5 + 1, n6 - 1, n4) != 2 || chunkManager.getBlockIdAt(n5, n6 - 1, n4 + 1) != 2 || chunkManager.getBlockIdAt(n5 - 1, n6 - 1, n4) != 2 || chunkManager.getBlockIdAt(n5, n6 - 1, n4 - 1) != 2 || chunkManager.getBlockIdAt(n5 + 2, n6 - 1, n4) != 2 || chunkManager.getBlockIdAt(n5, n6 - 1, n4 + 2) != 2 || chunkManager.getBlockIdAt(n5 - 2, n6 - 1, n4) != 2 || chunkManager.getBlockIdAt(n5, n6 - 1, n4 - 2) != 2 || chunkManager.getBlockIdAt(n5 - 3, n6 - 1, n4) != 2 || chunkManager.getBlockIdAt(n5, n6 - 1, n4 - 3) != 2 || chunkManager.getBlockIdAt(n5 + 2, n6, n4) != 0 || chunkManager.getBlockIdAt(n5, n6, n4 + 2) != 0 || chunkManager.getBlockIdAt(n5 + 3, n6, n4) != 0 || chunkManager.getBlockIdAt(n5, n6, n4 + 3) != 0) continue;
            new ObjectFallenTree(this.a, n5, n6, n4, this.b, nukkitRandom);
        }
    }

    private int a(int n, int n2) {
        int n3;
        int n4;
        for (n4 = 127; n4 > 0 && (n3 = this.a.getBlockIdAt(n, n4, n2)) != 2; --n4) {
        }
        return ++n4;
    }
}

