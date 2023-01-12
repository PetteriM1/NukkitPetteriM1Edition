/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.object.tree;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectBirchTree;
import cn.nukkit.math.NukkitRandom;

public class ObjectTallBirchTree
extends ObjectBirchTree {
    @Override
    public void placeObject(ChunkManager chunkManager, int n, int n2, int n3, NukkitRandom nukkitRandom) {
        this.treeHeight = nukkitRandom.nextBoundedInt(3) + 10;
        super.placeObject(chunkManager, n, n2, n3, nukkitRandom);
    }
}

