/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.structure;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;

public class ObjectFallenTree {
    public ObjectFallenTree(ChunkManager chunkManager, int n, int n2, int n3, int n4, NukkitRandom nukkitRandom) {
        this.placeObject(chunkManager, n, n2, n3, n4, nukkitRandom);
    }

    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, int n4, NukkitRandom nukkitRandom) {
        int n5 = nukkitRandom.nextRange(1, 2);
        if (n5 == 1 && n4 == 0) {
            chunkManager.setBlockAt(n - 1, n2, n3, 17);
            chunkManager.setBlockAt(n + 1, n2, n3, 17, 12);
            chunkManager.setBlockAt(n + 2, n2, n3, 17, 12);
            chunkManager.setBlockAt(n - 2, n2, n3, 106, 8);
            chunkManager.setBlockAt(n - 1, n2, n3 + 1, 106, 4);
            chunkManager.setBlockAt(n - 1, n2, n3 - 1, 106, 1);
        } else if (n5 == 1 && n4 == 1) {
            chunkManager.setBlockAt(n - 1, n2, n3, 17, 2);
            chunkManager.setBlockAt(n + 1, n2, n3, 17, 14);
            chunkManager.setBlockAt(n + 2, n2, n3, 17, 14);
            chunkManager.setBlockAt(n + 3, n2, n3, 17, 14);
            chunkManager.setBlockAt(n - 2, n2, n3, 106, 8);
            chunkManager.setBlockAt(n - 1, n2, n3 + 1, 106, 4);
            chunkManager.setBlockAt(n - 1, n2, n3 - 1, 106, 1);
            chunkManager.setBlockAt(n + 1, n2 + 1, n3, 39);
            chunkManager.setBlockAt(n + 3, n2 + 1, n3, 40);
        } else if (n5 == 2 && n4 == 0) {
            chunkManager.setBlockAt(n, n2, n3 - 1, 17);
            chunkManager.setBlockAt(n, n2, n3 + 1, 17, 12);
            chunkManager.setBlockAt(n, n2, n3 + 2, 17, 12);
            chunkManager.setBlockAt(n, n2, n3 - 2, 106, 1);
            chunkManager.setBlockAt(n + 1, n2, n3 - 1, 106, 2);
            chunkManager.setBlockAt(n - 1, n2, n3 - 1, 106, 8);
        } else if (n5 == 2 && n4 == 1) {
            chunkManager.setBlockAt(n, n2, n3 - 1, 17, 2);
            chunkManager.setBlockAt(n, n2, n3 + 1, 17, 14);
            chunkManager.setBlockAt(n, n2, n3 + 2, 17, 14);
            chunkManager.setBlockAt(n, n2, n3 + 3, 17, 14);
            chunkManager.setBlockAt(n, n2, n3 - 2, 106, 1);
            chunkManager.setBlockAt(n + 1, n2, n3 - 1, 106, 2);
            chunkManager.setBlockAt(n - 1, n2, n3 - 1, 106, 8);
            chunkManager.setBlockAt(n, n2 + 1, n3 + 1, 39);
            chunkManager.setBlockAt(n, n2 + 1, n3 + 3, 40);
        }
    }
}

