/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.ore;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitRandom;

public class OreType {
    public final int fullId;
    public final int clusterCount;
    public final int clusterSize;
    public final int maxHeight;
    public final int minHeight;
    public final int replaceBlockId;

    public OreType(Block block, int n, int n2, int n3, int n4) {
        this(block, n, n2, n3, n4, 1);
    }

    public OreType(Block block, int n, int n2, int n3, int n4, int n5) {
        this.fullId = block.getFullId();
        this.clusterCount = n;
        this.clusterSize = n2;
        this.maxHeight = n4;
        this.minHeight = n3;
        this.replaceBlockId = n5;
    }

    public boolean spawn(ChunkManager chunkManager, NukkitRandom nukkitRandom, int n, int n2, int n3, int n4) {
        float f2 = nukkitRandom.nextFloat() * (float)Math.PI;
        double d2 = (float)(n2 + 8) + MathHelper.sin(f2) * (float)this.clusterSize / 8.0f;
        double d3 = (float)(n2 + 8) - MathHelper.sin(f2) * (float)this.clusterSize / 8.0f;
        double d4 = (float)(n4 + 8) + MathHelper.cos(f2) * (float)this.clusterSize / 8.0f;
        double d5 = (float)(n4 + 8) - MathHelper.cos(f2) * (float)this.clusterSize / 8.0f;
        double d6 = n3 + nukkitRandom.nextBoundedInt(3) - 2;
        double d7 = n3 + nukkitRandom.nextBoundedInt(3) - 2;
        for (int k = 0; k < this.clusterSize; ++k) {
            float f3 = (float)k / (float)this.clusterSize;
            double d8 = d2 + (d3 - d2) * (double)f3;
            double d9 = d6 + (d7 - d6) * (double)f3;
            double d10 = d4 + (d5 - d4) * (double)f3;
            double d11 = nukkitRandom.nextDouble() * (double)this.clusterSize / 16.0;
            double d12 = (double)(MathHelper.sin((float)Math.PI * f3) + 1.0f) * d11 + 1.0;
            double d13 = (double)(MathHelper.sin((float)Math.PI * f3) + 1.0f) * d11 + 1.0;
            int n5 = MathHelper.floor(d8 - d12 / 2.0);
            int n6 = MathHelper.floor(d9 - d13 / 2.0);
            int n7 = MathHelper.floor(d10 - d12 / 2.0);
            int n8 = MathHelper.floor(d8 + d12 / 2.0);
            int n9 = MathHelper.floor(d9 + d13 / 2.0);
            int n10 = MathHelper.floor(d10 + d12 / 2.0);
            for (int i2 = n5; i2 <= n8; ++i2) {
                double d14 = ((double)i2 + 0.5 - d8) / (d12 / 2.0);
                if (!(d14 * d14 < 1.0)) continue;
                for (int i3 = n6; i3 <= n9; ++i3) {
                    double d15 = ((double)i3 + 0.5 - d9) / (d13 / 2.0);
                    if (!(d14 * d14 + d15 * d15 < 1.0)) continue;
                    for (int i4 = n7; i4 <= n10; ++i4) {
                        double d16 = ((double)i4 + 0.5 - d10) / (d12 / 2.0);
                        if (!(d14 * d14 + d15 * d15 + d16 * d16 < 1.0) || chunkManager.getBlockIdAt(i2, i3, i4) != this.replaceBlockId) continue;
                        chunkManager.setBlockFullIdAt(i2, i3, i4, this.fullId);
                    }
                }
            }
        }
        return true;
    }
}

