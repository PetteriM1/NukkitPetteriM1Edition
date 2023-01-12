/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.type;

import cn.nukkit.block.BlockID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitRandom;

public abstract class Populator
implements BlockID {
    public abstract void populate(ChunkManager var1, int var2, int var3, NukkitRandom var4, FullChunk var5);

    protected int getHighestWorkableBlock(ChunkManager chunkManager, int n, int n2, FullChunk fullChunk) {
        return fullChunk.getHighestBlockAt(n & 0xF, n2 & 0xF);
    }
}

