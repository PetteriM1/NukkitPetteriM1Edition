/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.PopulatorHelpers;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

public class PopulatorGrass
extends PopulatorSurfaceBlock {
    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return PopulatorHelpers.canGrassStay(n, n2, n3, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 497;
    }
}

