/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import java.util.List;

public class PopulatorSpring
extends Populator {
    private final int a;
    private final List<Integer> b;
    private final int d;
    private final int e;
    private final int c;

    public PopulatorSpring(int n, List<Integer> list, int n2, int n3, int n4) {
        this.a = n;
        this.b = list;
        this.d = n2;
        this.e = n3;
        this.c = n4;
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3 = n << 4;
        int n4 = n2 << 4;
        for (int k = 0; k < this.d; ++k) {
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11 = n3 + nukkitRandom.nextBoundedInt(16);
            int n12 = n4 + nukkitRandom.nextBoundedInt(16);
            int n13 = NukkitMath.randomRange(nukkitRandom, this.e, this.c);
            int n14 = chunkManager.getBlockIdAt(n11, n13, n12);
            if (n14 != 0 && !this.b.contains(n14) || (n10 = chunkManager.getBlockIdAt(n11, n13 - 1, n12)) == 12 || n10 == 13 || (n9 = chunkManager.getBlockIdAt(n11, n13 + 1, n12)) == 12 || n9 == 13 || !this.b.contains(n10) && !this.b.contains(n9) || (n8 = chunkManager.getBlockIdAt(n11 + 1, n13, n12)) == 12 || n8 == 13 || (n7 = chunkManager.getBlockIdAt(n11 - 1, n13, n12)) == 12 || n7 == 13 || (n6 = chunkManager.getBlockIdAt(n11, n13, n12 + 1)) == 12 || n6 == 13 || (n5 = chunkManager.getBlockIdAt(n11, n13, n12 - 1)) == 12 || n5 == 13) continue;
            int n15 = 0;
            if (this.b.contains(n8)) {
                ++n15;
            }
            if (this.b.contains(n7)) {
                ++n15;
            }
            if (this.b.contains(n6)) {
                ++n15;
            }
            if (this.b.contains(n5)) {
                ++n15;
            }
            if (n15 != 3) continue;
            int n16 = 0;
            if (n8 == 0) {
                ++n16;
            }
            if (n7 == 0) {
                ++n16;
            }
            if (n6 == 0) {
                ++n16;
            }
            if (n5 == 0) {
                ++n16;
            }
            if (n16 != 1) continue;
            chunkManager.setBlockAt(n11, n13, n12, this.a);
            Block block = Block.fullList[this.a << 4].clone();
            block.x = n11;
            block.y = n13;
            block.z = n12;
            block.level = fullChunk.getProvider().getLevel();
            if (block.level == null) continue;
            block.level.scheduleUpdate(block, block, 1, 0, false);
        }
    }
}

