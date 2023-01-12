/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectTree;
import cn.nukkit.math.NukkitRandom;

public class ObjectOakTree
extends ObjectTree {
    private int a = 7;

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getTreeHeight() {
        return this.a;
    }

    @Override
    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        this.a = nukkitRandom.nextBoundedInt(3) + 4;
        super.placeObject(chunkManager, n, n2, n3, nukkitRandom);
    }
}

