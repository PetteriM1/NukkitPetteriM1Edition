/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.Utils;

public class PopulatorPumpkin
extends Populator {
    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3;
        int n4;
        int n5;
        if (Utils.rand(0, 10) == 5 && (n5 = PopulatorPumpkin.a(fullChunk, n4 = nukkitRandom.nextRange(0, 15), n3 = nukkitRandom.nextRange(0, 15))) != -1 && PopulatorPumpkin.a(fullChunk, n4, n5, n3)) {
            fullChunk.setBlock(n4, n5, n3, 86);
        }
    }

    private static boolean a(FullChunk fullChunk, int n, int n2, int n3) {
        int n4 = fullChunk.getBlockId(n, n2, n3);
        return n4 == 0 && fullChunk.getBlockId(n, n2 - 1, n3) == 2;
    }

    private static int a(FullChunk fullChunk, int n, int n2) {
        int n3;
        int n4;
        for (n4 = 0; n4 <= 127 && (n3 = fullChunk.getBlockId(n, n4, n2)) != 0; ++n4) {
        }
        return n4 == 0 ? -1 : n4;
    }
}

