/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectTree;
import cn.nukkit.math.NukkitRandom;

public class ObjectSpruceTree
extends ObjectTree {
    protected int treeHeight = 15;

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getTreeHeight() {
        return this.treeHeight;
    }

    @Override
    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        this.treeHeight = nukkitRandom.nextBoundedInt(4) + 6;
        int n4 = this.treeHeight - (1 + nukkitRandom.nextBoundedInt(2));
        int n5 = 2 + nukkitRandom.nextBoundedInt(2);
        this.placeTrunk(chunkManager, n, n2, n3, nukkitRandom, this.treeHeight - nukkitRandom.nextBoundedInt(2));
        this.placeLeaves(chunkManager, n4, n5, n, n2, n3, nukkitRandom);
    }

    public void placeLeaves(ChunkManager chunkManager, int n, int n2, int n3, int n4, int n5, NukkitRandom nukkitRandom) {
        int n6 = nukkitRandom.nextBoundedInt(2);
        int n7 = 1;
        int n8 = 0;
        for (int k = 0; k <= n; ++k) {
            int n9 = n4 + this.treeHeight - k;
            for (int i2 = n3 - n6; i2 <= n3 + n6; ++i2) {
                int n10 = Math.abs(i2 - n3);
                for (int i3 = n5 - n6; i3 <= n5 + n6; ++i3) {
                    int n11 = Math.abs(i3 - n5);
                    if (n10 == n6 && n11 == n6 && n6 > 0 || Block.solid[chunkManager.getBlockIdAt(i2, n9, i3)]) continue;
                    chunkManager.setBlockAt(i2, n9, i3, this.getLeafBlock(), this.getType());
                }
            }
            if (n6 >= n7) {
                n6 = n8;
                n8 = 1;
                if (++n7 <= n2) continue;
                n7 = n2;
                continue;
            }
            ++n6;
        }
    }
}

