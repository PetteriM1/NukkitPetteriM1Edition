/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class PopulatorFlower
extends PopulatorSurfaceBlock {
    private final List<int[]> c = new ArrayList<int[]>();

    public void addType(int n, int n2) {
        int[] nArray = new int[]{n, n2};
        this.c.add(nArray);
    }

    public List<int[]> getTypes() {
        return this.c;
    }

    @Override
    protected void placeBlock(int n, int n2, int n3, int n4, FullChunk fullChunk, NukkitRandom nukkitRandom) {
        if (!this.c.isEmpty()) {
            int[] nArray = this.c.get(Utils.random.nextInt(this.c.size()));
            fullChunk.setFullBlockId(n, n2, n3, nArray[0] << 4 | nArray[1]);
            if (nArray[0] == 175) {
                fullChunk.setFullBlockId(n, n2 + 1, n3, nArray[0] << 4 | (8 | nArray[1]));
            }
        }
    }

    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureCover.ensureCover(n, n2, n3, fullChunk) && EnsureGrassBelow.ensureGrassBelow(n, n2, n3, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 0;
    }
}

