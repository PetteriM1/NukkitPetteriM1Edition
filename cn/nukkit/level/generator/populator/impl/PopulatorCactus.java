/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import java.util.concurrent.ThreadLocalRandom;

public class PopulatorCactus
extends PopulatorSurfaceBlock {
    private boolean a(int n, int n2, int n3, FullChunk fullChunk) {
        int n4 = fullChunk.getBlockId(n + BlockFace.NORTH.getXOffset() & 0xF, n2, n3 + BlockFace.NORTH.getZOffset() & 0xF);
        if (n4 != 0) {
            return false;
        }
        n4 = fullChunk.getBlockId(n + BlockFace.EAST.getXOffset() & 0xF, n2, n3 + BlockFace.EAST.getZOffset() & 0xF);
        if (n4 != 0) {
            return false;
        }
        n4 = fullChunk.getBlockId(n + BlockFace.SOUTH.getXOffset() & 0xF, n2, n3 + BlockFace.SOUTH.getZOffset() & 0xF);
        if (n4 != 0) {
            return false;
        }
        n4 = fullChunk.getBlockId(n + BlockFace.WEST.getXOffset() & 0xF, n2, n3 + BlockFace.WEST.getZOffset() & 0xF);
        return n4 == 0;
    }

    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureCover.ensureCover(n, n2, n3, fullChunk) && EnsureBelow.ensureBelow(n, n2, n3, 12, fullChunk) && this.a(n, n2, n3, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 1297;
    }

    @Override
    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        int n5 = ThreadLocalRandom.current().nextInt(3) + 1;
        if (n2 + n5 > 255) {
            return;
        }
        for (int k = 0; k < n5; ++k) {
            fullChunk.setFullBlockId(n, n2 + k, n3, n4);
        }
    }
}

