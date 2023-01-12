/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.structure.ObjectWell;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

public class PopulatorWell
extends Populator {
    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3;
        if (nukkitRandom.nextBoundedInt(190) != 1) {
            return;
        }
        int n4 = NukkitMath.randomRange(nukkitRandom, n << 4, (n << 4) + 15);
        int n5 = this.a(fullChunk, n4, n3 = NukkitMath.randomRange(nukkitRandom, n2 << 4, (n2 << 4) + 15));
        if (n5 == -1 || n5 > 128) {
            return;
        }
        new ObjectWell(chunkManager, n4, n5, n3);
    }

    private int a(FullChunk fullChunk, int n, int n2) {
        int n3;
        int n4;
        for (n4 = 127; n4 > 0 && (n3 = fullChunk.getBlockId(n & 0xF, n4 & 0xFF, n2 & 0xF)) != 12; --n4) {
        }
        return ++n4;
    }
}

