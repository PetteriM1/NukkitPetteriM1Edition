/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.biome.impl.iceplains;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.impl.iceplains.a;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

class b
extends Populator {
    private b() {
    }

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        for (int k = 0; k < 8; ++k) {
            int n3;
            int n4 = (n << 4) + nukkitRandom.nextBoundedInt(16);
            int n5 = (n2 << 4) + nukkitRandom.nextBoundedInt(16);
            boolean bl = nukkitRandom.nextBoundedInt(16) == 0;
            int n6 = 10 + nukkitRandom.nextBoundedInt(16) + (bl ? nukkitRandom.nextBoundedInt(31) : 0);
            int n7 = this.getHighestWorkableBlock(n4, n5, fullChunk);
            int n8 = n7 + n6;
            if (bl) {
                for (n3 = n7; n3 < n8; ++n3) {
                    chunkManager.setBlockAt(n4, n3, n5, 174);
                    chunkManager.setBlockAt(n4 + 1, n3, n5, 174);
                    chunkManager.setBlockAt(n4 - 1, n3, n5, 174);
                    chunkManager.setBlockAt(n4, n3, n5 + 1, 174);
                    chunkManager.setBlockAt(n4, n3, n5 - 1, 174);
                    if (nukkitRandom.nextBoolean()) {
                        chunkManager.setBlockAt(n4 + 1, n3, n5 + 1, 174);
                    }
                    if (nukkitRandom.nextBoolean()) {
                        chunkManager.setBlockAt(n4 + 1, n3, n5 - 1, 174);
                    }
                    if (nukkitRandom.nextBoolean()) {
                        chunkManager.setBlockAt(n4 - 1, n3, n5 + 1, 174);
                    }
                    if (!nukkitRandom.nextBoolean()) continue;
                    chunkManager.setBlockAt(n4 - 1, n3, n5 - 1, 174);
                }
                chunkManager.setBlockAt(n4 + 1, n8, n5, 174);
                chunkManager.setBlockAt(n4 - 1, n8, n5, 174);
                chunkManager.setBlockAt(n4, n8, n5 + 1, 174);
                chunkManager.setBlockAt(n4, n8, n5 - 1, 174);
                for (n3 = n8; n3 < n8 + 3; ++n3) {
                    chunkManager.setBlockAt(n4, n3, n5, 174);
                }
                continue;
            }
            n3 = nukkitRandom.nextBoundedInt(1) + 4;
            float f2 = (float)n3 / (float)n6;
            float f3 = n3;
            for (int i2 = n7; i2 < n8; ++i2) {
                int n9 = (int)(-f3);
                while ((float)n9 < f3) {
                    int n10 = (int)(-f3);
                    while ((float)n10 < f3) {
                        int n11 = (int)Math.sqrt(n9 * n9 + n10 * n10);
                        if ((int)f3 != n11 && nukkitRandom.nextBoolean()) {
                            chunkManager.setBlockAt(n4 + n9, i2, n5 + n10, 174);
                        }
                        ++n10;
                    }
                    ++n9;
                }
                f3 -= f2;
            }
        }
    }

    public int getHighestWorkableBlock(int n, int n2, FullChunk fullChunk) {
        return fullChunk.getHighestBlockAt(n & 0xF, n2 & 0xF) - 5;
    }

    b(a a2) {
        this();
    }
}

