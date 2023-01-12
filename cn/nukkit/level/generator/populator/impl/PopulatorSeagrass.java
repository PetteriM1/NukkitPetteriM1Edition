/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.type.PopulatorOceanFloorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

public class PopulatorSeagrass
extends PopulatorOceanFloorSurfaceBlock {
    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        int n4;
        return EnsureCover.ensureWaterCover(n, n2, n3, fullChunk) && ((n4 = fullChunk.getBlockId(n, n2 - 1, n3)) == 3 || n4 == 12 || n4 == 13);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 6160;
    }

    @Override
    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        if (n2 < 255 && nukkitRandom.nextDouble() < 0.3 && fullChunk.getBlockId(n, n2 + 1, n3) == 9) {
            fullChunk.setBlock(n, n2, n3, 385, 2);
            fullChunk.setBlock(n, n2 + 1, n3, 385, 1);
        } else {
            super.placeBlock(n, n2, n3, n4, fullChunk, nukkitRandom);
        }
    }
}

