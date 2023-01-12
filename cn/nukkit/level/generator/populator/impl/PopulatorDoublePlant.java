/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

public class PopulatorDoublePlant
extends PopulatorSurfaceBlock {
    private final int c;

    public PopulatorDoublePlant(int n) {
        this.c = n;
    }

    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return n2 < 255 && EnsureCover.ensureCover(n, n2, n3, fullChunk) && EnsureCover.ensureCover(n, n2 + 1, n3, fullChunk) && EnsureGrassBelow.ensureGrassBelow(n, n2, n3, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 0xAF0 | this.c;
    }

    @Override
    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        super.placeBlock(n, n2, n3, n4, fullChunk, nukkitRandom);
        fullChunk.setFullBlockId(n, n2 + 1, n3, 8 | n4);
    }
}

