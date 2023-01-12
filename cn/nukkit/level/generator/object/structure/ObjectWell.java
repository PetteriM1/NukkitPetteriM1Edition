/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.structure;

import cn.nukkit.level.ChunkManager;

public class ObjectWell {
    public ObjectWell(ChunkManager chunkManager, int n, int n2, int n3) {
        if (this.canPlaceObject(chunkManager, n, n2, n3)) {
            this.placeObject(chunkManager, n, n2, n3);
        }
    }

    public boolean canPlaceObject(ChunkManager chunkManager, int n, int n2, int n3) {
        int n4 = 0;
        for (int k = 0; k < 8; ++k) {
            if (k == 1 || k == 5) {
                ++n4;
            }
            for (int i2 = -n4; i2 < n4 + 1; ++i2) {
                for (int i3 = -n4; i3 < n4 + 1; ++i3) {
                    if (chunkManager.getBlockIdAt(n + i2, n2 + k, n3 + i3) == 0) continue;
                    return false;
                }
            }
        }
        return true;
    }

    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3) {
        for (int k = n2 - 3 + 5; k <= n2 + 5; ++k) {
            double d2 = k - (n2 + 5);
            int n4 = (int)(1.0 - d2 / 2.0);
            for (int i2 = n - n4; i2 <= n + n4; ++i2) {
                for (int i3 = n3 - n4; i3 <= n3 + n4; ++i3) {
                    chunkManager.setBlockAt(i2, k - 3, i3, 24);
                }
            }
        }
        chunkManager.setBlockAt(n + 2, n2, n3, 44, 1);
        chunkManager.setBlockAt(n - 2, n2, n3, 44, 1);
        chunkManager.setBlockAt(n, n2, n3 + 2, 44, 1);
        chunkManager.setBlockAt(n, n2, n3 - 2, 44, 1);
        chunkManager.setBlockAt(n, n2, n3, 8);
        chunkManager.setBlockAt(n + 1, n2, n3, 8);
        chunkManager.setBlockAt(n - 1, n2, n3, 8);
        chunkManager.setBlockAt(n, n2, n3 + 1, 8);
        chunkManager.setBlockAt(n, n2, n3 - 1, 8);
        chunkManager.setBlockAt(n, n2 + 1, n3, 0);
        chunkManager.setBlockAt(n + 1, n2 + 1, n3, 0);
        chunkManager.setBlockAt(n - 1, n2 + 1, n3, 0);
        chunkManager.setBlockAt(n, n2 + 1, n3 + 1, 0);
        chunkManager.setBlockAt(n, n2 + 1, n3 - 1, 0);
        chunkManager.setBlockAt(n, n2 + 2, n3, 0);
        chunkManager.setBlockAt(n + 1, n2 + 2, n3, 0);
        chunkManager.setBlockAt(n - 1, n2 + 2, n3, 0);
        chunkManager.setBlockAt(n, n2 + 2, n3 + 1, 0);
        chunkManager.setBlockAt(n, n2 + 2, n3 - 1, 0);
        chunkManager.setBlockAt(n, n2 + 3, n3, 24);
        chunkManager.setBlockAt(n + 1, n2 + 3, n3, 44, 1);
        chunkManager.setBlockAt(n - 1, n2 + 3, n3, 44, 1);
        chunkManager.setBlockAt(n, n2 + 3, n3 + 1, 44, 1);
        chunkManager.setBlockAt(n, n2 + 3, n3 - 1, 44, 1);
        chunkManager.setBlockAt(n + 1, n2 + 3, n3 + 1, 44, 1);
        chunkManager.setBlockAt(n + 1, n2 + 3, n3 - 1, 44, 1);
        chunkManager.setBlockAt(n - 1, n2 + 3, n3 - 1, 44, 1);
        chunkManager.setBlockAt(n - 1, n2 + 3, n3 + 1, 44, 1);
    }
}

