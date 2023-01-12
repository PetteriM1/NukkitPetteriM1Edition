/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

public class PopulatorGroundFire
extends PopulatorSurfaceBlock {
    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureCover.ensureCover(n, n2, n3, fullChunk) && EnsureBelow.ensureBelow(n, n2, n3, 87, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 816;
    }

    @Override
    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        super.placeBlock(n, n2, n3, n4, fullChunk, nukkitRandom);
        fullChunk.setBlockLight(n, n2, n3, Block.light[51]);
    }

    @Override
    protected int getHighestWorkableBlock(ChunkManager chunkManager, int n, int n2, FullChunk fullChunk) {
        int n3 = 0;
        for (int k = 0; k <= 127; ++k) {
            n3 = k;
            int n4 = fullChunk.getBlockId(n, k, n2);
            if (n4 == 0) break;
        }
        return n3 == 0 ? -1 : n3;
    }
}

