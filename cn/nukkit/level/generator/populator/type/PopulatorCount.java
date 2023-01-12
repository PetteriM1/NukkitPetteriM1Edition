/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.type;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

public abstract class PopulatorCount
extends Populator {
    private int a;
    private int b;

    public final void setRandomAmount(int n) {
        this.a = n + 1;
    }

    public final void setBaseAmount(int n) {
        this.b = n;
    }

    @Override
    public final void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3 = this.b + nukkitRandom.nextBoundedInt(this.a);
        for (int k = 0; k < n3; ++k) {
            this.populateCount(chunkManager, n, n2, nukkitRandom, fullChunk);
        }
    }

    protected abstract void populateCount(ChunkManager var1, int var2, int var3, NukkitRandom var4, FullChunk var5);
}

