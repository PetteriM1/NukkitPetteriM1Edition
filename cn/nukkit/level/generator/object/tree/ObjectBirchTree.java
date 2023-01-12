/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectTree;
import cn.nukkit.math.NukkitRandom;

public class ObjectBirchTree
extends ObjectTree {
    protected int treeHeight = 7;

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public int getTreeHeight() {
        return this.treeHeight;
    }

    @Override
    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        this.treeHeight = nukkitRandom.nextBoundedInt(2) + 5;
        super.placeObject(chunkManager, n, n2, n3, nukkitRandom);
    }
}

