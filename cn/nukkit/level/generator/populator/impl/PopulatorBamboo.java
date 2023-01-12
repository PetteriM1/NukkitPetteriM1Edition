/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.Utils;

public class PopulatorBamboo
extends PopulatorSurfaceBlock {
    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureCover.ensureCover(n, n2, n3, fullChunk) && EnsureBelow.ensureBelow(n, n2, n3, 2, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 6688;
    }

    @Override
    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        int n5;
        int n6;
        int n7 = Utils.rand(1, 10);
        for (n6 = 0; n6 < n7 && (n5 = n2 + n6) < 256 && fullChunk.getBlockId(n, n5, n3) == 0; ++n6) {
            super.placeBlock(n, n5, n3, n4, fullChunk, nukkitRandom);
        }
        n5 = n2 + n6;
        if (n5 < 256 && fullChunk.getBlockId(n, n5, n3) == 0) {
            fullChunk.setBlock(n, n5, n3, 418, 2);
        } else {
            fullChunk.setBlock(n, n5 - 1, n3, 418, 2);
        }
    }
}

