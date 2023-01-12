/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorSugarcane;
import cn.nukkit.math.NukkitRandom;
import java.util.concurrent.ThreadLocalRandom;

public class PopulatorTallSugarcane
extends PopulatorSugarcane {
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

