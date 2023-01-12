/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.mushroom.BigMushroom;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class MushroomPopulator
extends PopulatorCount {
    private final int c;

    public MushroomPopulator() {
        this(-1);
    }

    public MushroomPopulator(int n) {
        this.c = n;
    }

    @Override
    public void populateCount(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3;
        int n4 = n << 4 | nukkitRandom.nextBoundedInt(16);
        int n5 = this.getHighestWorkableBlock(chunkManager, n4, n3 = n2 << 4 | nukkitRandom.nextBoundedInt(16), fullChunk);
        if (n5 != -1) {
            new BigMushroom(this.c).generate(chunkManager, nukkitRandom, new Vector3(n4, n5, n3));
        }
    }

    @Override
    protected int getHighestWorkableBlock(ChunkManager chunkManager, int n, int n2, FullChunk fullChunk) {
        int n3;
        int n4;
        n &= 0xF;
        n2 &= 0xF;
        for (n4 = 254; n4 > 0 && (n3 = fullChunk.getBlockId(n, n4, n2)) != 3 && n3 != 2 && n3 != 110; --n4) {
            if (n3 == 0 || n3 == 78) continue;
            return -1;
        }
        return ++n4;
    }
}

