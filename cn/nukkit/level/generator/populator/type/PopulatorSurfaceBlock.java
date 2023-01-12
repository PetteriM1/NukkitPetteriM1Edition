/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.type;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.PopulatorHelpers;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.NukkitRandom;

public abstract class PopulatorSurfaceBlock
extends PopulatorCount {
    @Override
    protected void populateCount(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3;
        int n4 = nukkitRandom.nextBoundedInt(16);
        int n5 = this.getHighestWorkableBlock(chunkManager, n4, n3 = nukkitRandom.nextBoundedInt(16), fullChunk);
        if (n5 > 0 && this.canStay(n4, n5, n3, fullChunk)) {
            this.placeBlock(n4, n5, n3, this.getBlockId(n4, n3, nukkitRandom, fullChunk), fullChunk, nukkitRandom);
        }
    }

    protected abstract boolean canStay(int var1, int var2, int var3, FullChunk var4);

    protected abstract int getBlockId(int var1, int var2, NukkitRandom var3, FullChunk var4);

    @Override
    protected int getHighestWorkableBlock(ChunkManager chunkManager, int n, int n2, FullChunk fullChunk) {
        int n3;
        for (n3 = 254; n3 >= 0 && PopulatorHelpers.isNonSolid(fullChunk.getBlockId(n, n3, n2)); --n3) {
        }
        return n3 == 0 ? -1 : ++n3;
    }

    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        fullChunk.setFullBlockId(n, n2, n3, n4);
    }
}

