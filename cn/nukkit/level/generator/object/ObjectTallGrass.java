/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class ObjectTallGrass {
    public static void growGrass(ChunkManager chunkManager, Vector3 vector3, NukkitRandom nukkitRandom) {
        block0: for (int k = 0; k < 128; ++k) {
            int n = 0;
            int n2 = vector3.getFloorX();
            int n3 = vector3.getFloorY() + 1;
            int n4 = vector3.getFloorZ();
            while (true) {
                if (n >= k >> 4) {
                    if (chunkManager.getBlockIdAt(n2, n3, n4) != 0) continue block0;
                    if (nukkitRandom.nextBoundedInt(8) == 0) {
                        if (nukkitRandom.nextBoolean()) {
                            chunkManager.setBlockAt(n2, n3, n4, 37);
                            continue block0;
                        }
                        chunkManager.setBlockAt(n2, n3, n4, 38);
                        continue block0;
                    }
                    chunkManager.setBlockAt(n2, n3, n4, 31, 1);
                    continue block0;
                }
                if (chunkManager.getBlockIdAt(n2 += nukkitRandom.nextRange(-1, 1), (n3 += nukkitRandom.nextRange(-1, 1) * nukkitRandom.nextBoundedInt(3) >> 1) - 1, n4 += nukkitRandom.nextRange(-1, 1)) != 2 || n3 > 255 || n3 < 0) continue block0;
                ++n;
            }
        }
    }
}

