/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.type.PopulatorOceanFloorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.Utils;

public class PopulatorKelp
extends PopulatorOceanFloorSurfaceBlock {
    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureCover.ensureWaterCover(n, n2, n3, fullChunk) && EnsureBelow.ensureBelow(n, n2, n3, 13, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 6288;
    }

    @Override
    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        int n5;
        int n6 = Utils.rand(1, 25);
        for (int k = 0; k < n6 && (n5 = n2 + k) < 256 && fullChunk.getBlockId(n, n5, n3) == 9 && fullChunk.getBlockId(n, n5 + 1, n3) == 9; ++k) {
            super.placeBlock(n, n5, n3, n4, fullChunk, nukkitRandom);
        }
    }
}

