/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.TreeGenerator;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public abstract class HugeTreesGenerator
extends TreeGenerator {
    protected final int baseHeight;
    protected final Block woodMetadata;
    protected final Block leavesMetadata;
    protected int extraRandomHeight;

    public HugeTreesGenerator(int n, int n2, Block block, Block block2) {
        this.baseHeight = n;
        this.extraRandomHeight = n2;
        this.woodMetadata = block;
        this.leavesMetadata = block2;
    }

    protected int getHeight(NukkitRandom nukkitRandom) {
        int n = nukkitRandom.nextBoundedInt(3) + this.baseHeight;
        if (this.extraRandomHeight > 1) {
            n += nukkitRandom.nextBoundedInt(this.extraRandomHeight);
        }
        return n;
    }

    private boolean a(ChunkManager chunkManager, Vector3 vector3, int n) {
        boolean bl = true;
        if (vector3.getY() >= 1.0 && vector3.getY() + (double)n + 1.0 <= 256.0) {
            for (int k = 0; k <= 1 + n; ++k) {
                int n2 = 2;
                if (k == 0) {
                    n2 = 1;
                } else if (k >= 1 + n - 2) {
                    n2 = 2;
                }
                for (int i2 = -n2; i2 <= n2 && bl; ++i2) {
                    for (int i3 = -n2; i3 <= n2 && bl; ++i3) {
                        Vector3 vector32 = vector3.add(i2, k, i3);
                        if (!(vector3.getY() + (double)k < 0.0) && !(vector3.getY() + (double)k >= 256.0) && this.canGrowInto(chunkManager.getBlockIdAt((int)vector32.x, (int)vector32.y, (int)vector32.z))) continue;
                        bl = false;
                    }
                }
            }
            return bl;
        }
        return false;
    }

    private boolean a(Vector3 vector3, ChunkManager chunkManager) {
        Vector3 vector32 = vector3.getSideVec(BlockFace.DOWN);
        int n = chunkManager.getBlockIdAt((int)vector32.x, (int)vector32.y, (int)vector32.z);
        if ((n == 2 || n == 3) && vector3.getY() >= 2.0) {
            this.setDirtAt(chunkManager, vector32);
            this.setDirtAt(chunkManager, vector32.east());
            this.setDirtAt(chunkManager, vector32.south());
            this.setDirtAt(chunkManager, vector32.south().east());
            return true;
        }
        return false;
    }

    protected boolean ensureGrowable(ChunkManager chunkManager, Vector3 vector3, int n) {
        return this.a(chunkManager, vector3, n) && this.a(vector3, chunkManager);
    }

    protected void growLeavesLayerStrict(ChunkManager chunkManager, Vector3 vector3, int n) {
        int n2 = n * n;
        for (int k = -n; k <= n + 1; ++k) {
            for (int i2 = -n; i2 <= n + 1; ++i2) {
                int n3 = k - 1;
                int n4 = i2 - 1;
                if (k * k + i2 * i2 > n2 && n3 * n3 + n4 * n4 > n2 && k * k + n4 * n4 > n2 && n3 * n3 + i2 * i2 > n2) continue;
                Vector3 vector32 = vector3.add(k, 0.0, i2);
                int n5 = chunkManager.getBlockIdAt((int)vector32.x, (int)vector32.y, (int)vector32.z);
                if (n5 != 0 && n5 != 18) continue;
                this.setBlockAndNotifyAdequately(chunkManager, vector32, this.leavesMetadata);
            }
        }
    }

    protected void growLeavesLayer(ChunkManager chunkManager, Vector3 vector3, int n) {
        int n2 = n * n;
        for (int k = -n; k <= n; ++k) {
            for (int i2 = -n; i2 <= n; ++i2) {
                if (k * k + i2 * i2 > n2) continue;
                Vector3 vector32 = vector3.add(k, 0.0, i2);
                int n3 = chunkManager.getBlockIdAt((int)vector32.x, (int)vector32.y, (int)vector32.z);
                if (n3 != 0 && n3 != 18) continue;
                this.setBlockAndNotifyAdequately(chunkManager, vector32, this.leavesMetadata);
            }
        }
    }
}

