/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectSpruceTree;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.Utils;

public class ObjectBigSpruceTree
extends ObjectSpruceTree {
    private final float b;
    private final int c;
    private final boolean a;

    public ObjectBigSpruceTree(float f2, int n) {
        this(f2, n, false);
    }

    public ObjectBigSpruceTree(float f2, int n, boolean bl) {
        this.b = f2;
        this.c = n;
        this.a = bl;
    }

    @Override
    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        this.treeHeight = this.a ? Utils.rand(15, 20) : nukkitRandom.nextBoundedInt(15) + 20;
        int n4 = this.treeHeight - (int)((float)this.treeHeight * this.b);
        int n5 = this.c + nukkitRandom.nextBoundedInt(2);
        this.placeTrunk(chunkManager, n, n2, n3, nukkitRandom, this.getTreeHeight() - nukkitRandom.nextBoundedInt(3));
        this.placeLeaves(chunkManager, n4, n5, n, n2, n3, nukkitRandom);
    }

    @Override
    protected void placeTrunk(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom, int n4) {
        chunkManager.setBlockAt(n, n2 - 1, n3, 3);
        int n5 = 2;
        for (int k = 0; k < n4; ++k) {
            for (int i2 = 0; i2 < n5; ++i2) {
                for (int i3 = 0; i3 < n5; ++i3) {
                    int n6 = chunkManager.getBlockIdAt(n, n2 + k, n3);
                    if (!this.overridable(n6)) continue;
                    chunkManager.setBlockAt(n + i2, n2 + k, n3 + i3, this.getTrunkBlock(), this.getType());
                }
            }
        }
    }
}

