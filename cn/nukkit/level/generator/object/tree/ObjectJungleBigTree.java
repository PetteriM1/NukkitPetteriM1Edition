/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.HugeTreesGenerator;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class ObjectJungleBigTree
extends HugeTreesGenerator {
    public ObjectJungleBigTree(int n, int n2, Block block, Block block2) {
        super(n, n2, block, block2);
    }

    @Override
    public boolean generate(ChunkManager chunkManager, NukkitRandom nukkitRandom, Vector3 vector3) {
        int n = this.getHeight(nukkitRandom);
        if (!this.ensureGrowable(chunkManager, vector3, n)) {
            return false;
        }
        this.b(chunkManager, vector3.up(n), 2);
        int n2 = (int)vector3.getY() + n - 2 - nukkitRandom.nextBoundedInt(4);
        while ((double)n2 > vector3.getY() + (double)(n >> 1)) {
            int n3;
            float f2 = nukkitRandom.nextFloat() * ((float)Math.PI * 2);
            int n4 = (int)(vector3.getX() + (double)(0.5f + MathHelper.cos(f2) * 4.0f));
            int n5 = (int)(vector3.getZ() + (double)(0.5f + MathHelper.sin(f2) * 4.0f));
            for (n3 = 0; n3 < 5; ++n3) {
                n4 = (int)(vector3.getX() + (double)(1.5f + MathHelper.cos(f2) * (float)n3));
                n5 = (int)(vector3.getZ() + (double)(1.5f + MathHelper.sin(f2) * (float)n3));
                this.setBlockAndNotifyAdequately(chunkManager, new Vector3(n4, n2 - 3 + (n3 >> 1), n5), this.woodMetadata);
            }
            n3 = 1 + nukkitRandom.nextBoundedInt(2);
            for (int k = n2 - n3; k <= n2; ++k) {
                int n6 = k - n2;
                this.growLeavesLayer(chunkManager, new Vector3(n4, k, n5), 1 - n6);
            }
            n2 -= 2 + nukkitRandom.nextBoundedInt(4);
        }
        for (n2 = 0; n2 < n; ++n2) {
            Vector3 vector32 = vector3.up(n2);
            if (this.canGrowInto(chunkManager.getBlockIdAt((int)vector32.x, (int)vector32.y, (int)vector32.z))) {
                this.setBlockAndNotifyAdequately(chunkManager, vector32, this.woodMetadata);
                if (n2 > 0) {
                    this.a(chunkManager, nukkitRandom, vector32.west(), 8);
                    this.a(chunkManager, nukkitRandom, vector32.north(), 1);
                }
            }
            if (n2 >= n - 1) continue;
            Vector3 vector33 = vector32.east();
            if (this.canGrowInto(chunkManager.getBlockIdAt((int)vector33.x, (int)vector33.y, (int)vector33.z))) {
                this.setBlockAndNotifyAdequately(chunkManager, vector33, this.woodMetadata);
                if (n2 > 0) {
                    this.a(chunkManager, nukkitRandom, vector33.east(), 2);
                    this.a(chunkManager, nukkitRandom, vector33.north(), 1);
                }
            }
            Vector3 vector34 = vector32.south().east();
            if (this.canGrowInto(chunkManager.getBlockIdAt((int)vector34.x, (int)vector34.y, (int)vector34.z))) {
                this.setBlockAndNotifyAdequately(chunkManager, vector34, this.woodMetadata);
                if (n2 > 0) {
                    this.a(chunkManager, nukkitRandom, vector34.east(), 2);
                    this.a(chunkManager, nukkitRandom, vector34.south(), 4);
                }
            }
            Vector3 vector35 = vector32.south();
            if (!this.canGrowInto(chunkManager.getBlockIdAt((int)vector35.x, (int)vector35.y, (int)vector35.z))) continue;
            this.setBlockAndNotifyAdequately(chunkManager, vector35, this.woodMetadata);
            if (n2 <= 0) continue;
            this.a(chunkManager, nukkitRandom, vector35.west(), 8);
            this.a(chunkManager, nukkitRandom, vector35.south(), 4);
        }
        return true;
    }

    private void a(ChunkManager chunkManager, NukkitRandom nukkitRandom, Vector3 vector3, int n) {
        if (nukkitRandom.nextBoundedInt(3) > 0 && chunkManager.getBlockIdAt((int)vector3.x, (int)vector3.y, (int)vector3.z) == 0) {
            this.setBlockAndNotifyAdequately(chunkManager, vector3, Block.get(106, n));
        }
    }

    private void b(ChunkManager chunkManager, Vector3 vector3, int n) {
        for (int k = -2; k <= 0; ++k) {
            this.growLeavesLayerStrict(chunkManager, vector3.up(k), n + 1 - k);
        }
    }
}

